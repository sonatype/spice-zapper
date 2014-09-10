<!--

    Copyright (c) 2007-2014 Sonatype, Inc. All rights reserved.

    This program is licensed to you under the Apache License Version 2.0,
    and you may not use this file except in compliance with the Apache License Version 2.0.
    You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.

    Unless required by applicable law or agreed to in writing,
    software distributed under the Apache License Version 2.0 is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.

-->
Zapper
======

Zapper is small utility for simple but fast content upload and download over some existing application protocol (primarily focused on HTTP, but other protocols like SCP or FTP might work too).

Original use case: imagine you have a _set of files_ that you want to transport over the wire. Those files belong to a logical whole, and you want to handle (upload or download, you want "all or nothing") them as _logically one_ entity. Also, the files in the set might totally differ in their properties: you might have some small files, few bytes long ones, some long text/XML/JSON files that compress well, and huge binary uncompressed and compressed ones, and even gigantic -- talking about gigabytes -- files.

The worst case is upload them (in case of HTTP with PUT for example) one-by-one. The total time you spend uploading will be sum of each upload times (there are still even worse cases, like HTTP/1.0 vs HTTP/1.1 with connection reestablished per request etc). Also, in this case, the actual transfer _does not handle the files as logically one_: you are the one who needs to take care that operation ends as "all or nothing", the error handling, possible recovery is your burden.

Next case would be to have some sort of _one HTTP request_ but carrying _all the files_. Typically, like MIME Multipart body. Drawback of this approach is that in case you need to transport 1G, you'd actually transport 1.33G over the wire due to Base64 encoding overhead. Moreover, you need some
server side support for this too.

Okay, then let's ZIP them (compressed or just stored, whatever), and have single stream of bytes for single HTTP request. Sounds okay, but still, the transport would take a bit less then the first case (one HTTP request per one file), and there is no connection and HTTP request-response roundtrip,
but you still transport all the bytes over the wire sequentially. Also, this solution would need support on server side: something that receives ZIP, checks for transport errors, unpacks etc.

And, all these above you usually has to implement from scratch, but just that: transport consistency, error handling, operation resume, all is on your desk.

Zapper protocols
================

Zapper intends to implement 3 basic "protocols":

* "whole-file" - this is the simplest and "most compatible" protocol for uploading and downloading, as it does not require any server-side support. All it does is simply starts N (configurable) worker threads, and each of one pulls one file from the "pool of uploadable/downloadable files" and does the job.
All of them iterate as long as there are files in the pool. This protocol works the best if the files you upload are pretty much the same size. Naturally, worst case (where this protocol does not help much) is when N=2 and you upload total of 1G distributed in 2 files: one of 0.9G and one of 0.1G.
* "ranged-file" - similar to zapper, but transport integrity checks are less enforced: it needs a remote server (HTTP for example) that supports "ranges" (like for HTTP Servers the "Accept-Ranges: bytes" header). Algorithm of "segmenting" is similar as for "zapper" protocol.
* "zapper" - this protocol needs server side support, and works similar to BitTorrent but in P2P fashion: it "segments" the files in pretty much same sized segments, and distributes the segment upload/download over N threads. All segments, each file and total transport is also checked for integrity. The segment size is adjustable, and can be easily adapted to "best fit", it's not wired to 1MB or so.

Examples (segment max size is for example's clarity set to some size, but it's variable. The longer the string behind "Tx=" the longer the transport takes):

```
  File to upload (chars stands for one MB, HTTP requests are in braces):
    fileA = 1MB
    fileB = 1MB
    N = 2
    
  whole-file protocol:
    T1 = {A}
    T2 = {B}
  zapper protocol (segment max size 1MB):
    T1 = {A}
    T2 = {B}
```

```
  File to upload (chars stands for one MB, HTTP requests are in braces):
    fileA = 1MB
    fileB = 10MB
    N = 2

  whole-file protocol:
    T1 = {A}
    T2 = {BBBBBBBBBB}
  zapper protocol (segment max size 2MB):
    T1 = {A}{BB}{BB}
    T2 = {BB}{BB}{BB}
```

Examples of zapper vs whole-file protocol for same payload and different N:

```
  File to upload (chars stands for one MB, HTTP requests are in braces):
    fileA = 1MB
    fileB = 10MB
    fileC = 15MB

  N = 2
  whole-file protocol:
    T1 = {A}{CCCCCCCCCCCCCCC}
    T2 = {BBBBBBBBBB}
  zapper protocol (segment size 4MB):
    T1 = {A}{BBBB}{CCCC}{CCCC}
    T2 = {BBBB}{BB}{CCCC}{CCC}

  N = 4
  whole-file protocol:
    T1 = {A}
    T2 = {BBBBBBBBBB}
    T3 = {CCCCCCCCCCCCCCC}
    T4 =
  zapper protocol (segment size 4MB):
    T1 = {A}{CCCC}
    T2 = {BBBB}{CCCC}
    T3 = {BBBB}{CCCC}
    T4 = {BB}{CCC}

  N = 6
  whole-file protocol:
    T1 = {A}
    T2 = {BBBBBBBBBB}
    T3 = {CCCCCCCCCCCCCCC}
    T4 =
    T5 =
    T6 =
  zapper protocol (segment size 4MB):
    T1 = {A}{CCCC}
    T2 = {BBBB}
    T3 = {BBBB}
    T4 = {BB}{CCC}
    T5 = {CCCC}
    T6 = {CCCC}
```

As you see, with smaller N, zapper protocol might get worse than whole-file (doe to many request roundtrips), but as N grows, it even halves the transport time. Default N in Zapper is 6.

"WTF is Zapper"?
====

In short, Zapper makes all these approaches above supported out of the box (with or without server side support, is detected transparently), but if Zapper is supported by server side too, you have one more options:

* chop files into "segments". Segment size is variable, every file smaller than some "configurable threshold" is taken as one segment, but files larger than "configurable threshold" are segmented into multiple segments.
* upload the payloads segment-by-segment over configurable count of "tracks" (ran by threads) to achieve "best payload distribution", every track is made nearly same sized.
* upload assembled tracks in parallel, every track (might be one or more segment) is one actual connection (HTTP Request) to server
* reassemble the payload back into files on server side, verify success by integrity on segment, file and whole transport level

If you leave out "chopping" (files _are the segments_, make them into 1:1 segments), you end up with simplest (and most compatible) protocol Zapper supports: "whole-file". It simply uploads all the files in parallel, over fixed count of "tracks" (here, transfer time will be max time of a track, that might peak if you have huge files present).

How it works
------------

Zapper has two sides: client and server side.

The client side is rather simple to use: you need to grab a "handle" to the given server side endpoint, and perform an "upload" or "download". Zapper handles all for you behind the curtains. You can transfer (up and down) a single "file", or a bunch of files in single transaction. Zapper ensures that transaction ends properly and transport did not corrupt the files (it uses pluggable hashing for checking the content, by default SHA1).

"File" and "Directory" may refer to actual OS files and directories, but does not have to. Everyone can roll their own implementations, but `java.io.File` based implementations are provided out of the box.

(TBD) The server side is implemented in a way to not have any dependency on actual server side being run (HTTP, J2EE Servlet, etc), so it might even work as Servlet, but also FTPlet within Apache FTP Server is considered. Main goal was to make this work over HTTP.

The initial setup is following: on upload, client side "enumerates" the Files to be uploaded, sums their total size, and based on count of connections to use (configurable), does a "weighed distribution" of the payload segments over connections. Then client sends this "message" -- the list of payload and segments mapping -- to server, where it ack it, and sends back a "job ticket". And the actual upload happens in parallel, of the segmented payload, on multiple channels for fastest possible transfer. Channels belonging to single transaction are all equipped with "job ticket". Then, on server side, the segments are "reassembled" into initial payload.

In case of download, similarly, client asks for a download, server side "enumerates" the payload, sends the list of payload and segments to client, together with "job ticket", and client starts the download on multiple channels.

Notes
-----

Only "whole-file" upload is implemented so far!


Ideas
-----

* Filters -- filter munges the content, and might be applied per segment, per track or per whole ZFile (once assembled as whole). They might do things like compressing content, encrypting it or whatever.
* "Optimization" -- if for some reason (ie. you send generated files), you are about to send multiple files having _same content_, Zapper will send the content only once, but generate multiple files for you at arrival.
