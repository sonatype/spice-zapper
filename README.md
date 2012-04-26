Zapper
======

Zapper is small utility for simple but fast content upload and download over some existing application protocol (primarily focused on HTTP, but other protocols like SCP or FTP might work too).

Original use case: imagine you have a _set of files_ that you want to transport over the wire. Those files belong to a logical whole, and you want to handle them as _logically one_ entity. Also, the files in the set might totally differ in their properties: you might have some small files, few bytes long ones, some long text/XML/JSON files that compress well, and huge binary uncompressed and compressed ones, and even gigantic -- talking about gigabytes -- files.

If, for a moment, we stay with HTTP protocol, you have these options:

* Serialized HTTP/1.0 requests - this is the "dumbest" but also simplest way to transport them. While remote might not be HTTP/1.0 server, clients are usually still doing this: establish connection, perform the PUT/POST, tear down connection. Slow, as it takes sum(f1..fn). No error recovery (unless you code per file one). Now client nor server actually handles upload as _logically one_ entity. Typically, this transport is used by "lightweight HTTP Wagon"", a transport used by Maven2 to perform deploys.
* Parallel HTTP/1.0 requests - a bit better than previous one. Almost same stands as for serialized one, there is also the burden on your side to code threading and/or async stuff. Ideally, the transport time here in ideal circumstances (each file upload starts together, you have as many requests as many files) gets to max(f1..fn). Clearly, this is not ideal if you files with "unbalanced sizes": one file of 2kB and one of 600MB, parallelization does not buys you much. But opening large number of requests is not always feasible, as opening 100 simultaneous requests might be detected as "attack" on server side.
* Serialized HTTP/1.1 requests - benefit of this over HTTP/1.0 one is to save the time needed to establish TCP session, as connection might be reused (naturally, this needs your side to obey this too, usually not a problem with HTTP client libraries).
* Parallel HTTP/1.1 requests - almost same as parallel HTTP/1.0 with the benefit of reused connection.
* HTTP Range use, spawn multiple requests sending/receiving part ranges.

And one more:

* to have one request, that would have a MIME Multipart body holding all the files. This is actually the worse of all the solutions above, due to needed Base64 encoding of files being "parts", ending up in even more bytes being transferred than your payload is.

Problems with these above is you need to code it, and while these above are "most compatible" ways to perform this task, as there is no need to have any "server side" component (except a normal HTTP Server, which is not a problem), you still _do not have_ your main goal: to have files handled as logically one set.

"WTF is Zapper"?
====

Zapper makes a step further: these approaches above are all supported by it, but if zapper is supported by server side too (it's transparently detected by the library), you have one more options:

* chop files into "segments". Segment size is variable, every file smaller than some "configurable threshold" is taken as one segment, but files larger than "configurable threshold" are segmented into multiple segments.
* upload the payloads segment-by-segment over configurable count of "tracks" to achieve "best payload distribution", every track is made nearly same sized.
* upload assembled tracks in parallel, every track (might be one or more segment) is one actual connection (HTTP Request) to server
* reassemble the payload back into files on server side, verify success by hashing

If you leave out "chopping" (files are made into 1:1 segments), you end up with simplest (and most compatible) protocol Zapper supports: "whole-file". It simply uploads all the files in parallel, over fixed count of "tracks" (here, transfer time will be max time of a track, that might peak if you have huge files present).


How it works
------------

Zapper has two sides: client and server side.

The client side is rather simple to use: you need to grab a "handle" to the given server side endpoint, and perform an "upload" or "download". Zapper handles all for you behind the curtains. You can transfer (up and down) a single "file", or a bunch of files in single transaction. Zapper ensures that transaction ends properly and transport did not corrupt the files (it uses pluggable hashing for checking the content, by default SHA1).

"File" and "Directory" may refer to actual OS files and directories, but does not have to. Everyone can roll their own implementations, but `java.io.File` based implementations are provided out of the box.

(TBD) The server side is implemented in a way to not have any dependency on actual server side being run (HTTP, J2EE Servlet, etc), so it might even work as Servlet, but also as FTPlet within Apache FTP Server. Main goal was to make this work over HTTP.

The initial setup is following: on upload, client side "enumerates" the Files to be uploaded, sums their total size, and based on count of connections to use (configurable), does a "weighed distribution" of the payload segments over connections. Then client sends this "message" -- the list of payload and segments mapping -- to server, where it ack it, and sends back a "job ticket". And the actual upload happens in parallel, of the segmented payload, on multiple channels for fastest possible transfer. Channels belonging to single transaction are all equipped with "job ticket". Then, on server side, the segments are "reassembled" into initial payload.

In case of download, similarly, client asks for a download, server side "enumerates" the payload, sends the list of payload and segments to client, together with "job ticket", and client starts the download on multiple channels.

Protocol
--------

For uploading content:

0) If remote is detected as non-zapper server, Zapper Clients falls back to most compatible "whole-file" protocol, simple parallel uploads and downloads. The parameter "max track count" in this case is used to limit max connection count.

Nothing below this is implemented yet.

1) Client: assembles the payload (that consists of single or multiple ZFiles). Calculates the "segments", and assembles the "recipe", that contains file(seg1, seg2, …) mappings for all files in the payload. Calculates hashes for all files and segments, and using those creates references in "recipe". Recipe contains the channel numbers clients want to use (configurable).

2) Client uploads the recipe to server, and waits for response.

3) Server receives the recipe, acknowledges it, and responds to client with "ok" or some error. In case of "ok", the response contains the "adjusted" number of channels to use, might be same as client, or might be less. Response contains the "job ticket too".

4) Client opens as many channels as told by server, and starts pushing the segments.

5) Server receives the segments and stores them to temporary storage.

6) On transfer end, client simply hangs up (as server has all the info needed to perform success validation of the transfer).

7) Server, using the recipe, and by calculating checksums as it was receiving the segments, verifies that all the segments has right checksums. Then, using recipe, starts "assembling" the payload back into it's original form (file or files). Finally, it verifies that outcome (the payload) checksums matches those in the original recipe, and hands over the uploaded content to some subsystem (into what Zapper is integrated on server side).

Ideas
-----

* Filters -- filter munges the content, and might be applied per segment, per track or per whole ZFile (once assembled as whole). They might do things like compressing content, encrypting it or whatever.
* "Optimization" -- if for some reason (ie. you send generated files), you are about to send multiple files having _same content_, Zapper will send the content only once, but generate multiple files for you at arrival.
