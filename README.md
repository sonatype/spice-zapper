Zapper
======

Zapper is small utility for high scalable content upload and download over some protocol (HTTP for sure, others to come). Is powered by Ning's AsyncHttpClient (great work JF!).

How it works
------------

Zapper has two sides: client and server side.

The client side is rather simple to use: you need to grab a "handle" to the given server side endpoint, and perform an "upload" of "download". Zapper handles all for you behind the curtains. You can transfer (up and down) a single "file", or a bunch of files (called a "Directory" in Zapper lingo) in single transaction. Zapper ensures that transaction ends properly and transport did not corrupt the files (it uses SHA1 hashing for checking the content).

"File" and "Directory" may refer to actual OS files and directories, but does not have to. Everyone can roll their own implementations, but java.io.File based implementations are provided out of the box.

The server side is implemented in a way to not have any dependency on actual server side being run (HTTP, J2EE Servlet, etc), so it might even work as Servlet, but also as FTPlet within Apache FTP Server. Main goal was to make this work over HTTP.

The initial setup is following: on upload, client side "enumerates" the Files to be uploaded, sums their total size, and based on connections to use (configurable), does a "weighed distribution" of the payload segments over connections. Then client sends this "message" -- the list of payload and segments mapping -- to server, where it ack it, and sends back a "job ticket". And the actual upload happens in parallel, of the segmented payload, on multiple channels for fastest possible transfer. Channels belonging to single transaction are all equipped with "job ticket". Then, on server side, the segments are "reassembled" into initial payload.

In case of download, similarly, client asks for a download, server side "enumerates" the payload, sends the list of payload and segments to client, together with "job ticket", and client starts the download on multiple channels.

Protocol
--------

For uploading content:

0) If remote is detected as non-zapper server, Zapper Clients falls back to simple serial PUTs and GETs.

1) Client: assembles the payload (that consists of single or multiple ZFiles). Calculates the "segments", and assembles the "recipe", that contains file(seg1, seg2, …) mappings for all files in the payload. Calculates hashes for all files and segments, and using those creates references in "recipe". Recipe contains the channel numbers clients want to use (configurable).

2) Clien uploads the recipe to server, and waits for response.

3) Server receives the recipe, acknowledges it, and responds to client with "ok" or some error. In case of "ok", the response contains the "adjusted" number of channels to use, might be same as client, or might be less. Response contains the "job ticket too".

4) Client opens as many channels as told by server, and starts pushing the segments.

5) Server receives the segments and stores them to temporary storage.

6) On transfer end, client simply hangs up (as server has all the info needed to perform success validation of the transfer).

7) Server, using the recipe, and by calculating checksums as it was receiving the segments, verifies that all the segments has right checksums. Then, using recipe, starts "assembling" the payload back into it's original form (file or files). Finally, it verifies that outcome (the payload) checksums matches those in the original recipe, and hands over the uploaded content to some subsystem (into what Zapper is integrated on server side).

Ideas
-----

* Filters -- filter munges the content, and might be applied per segment, per track or per whole ZFile (once assembled as whole). They might do things like compressing content, encrypting it or whatever.
* "Optimization" -- if for some reason (ie. you send generated files), you are about to send multiple files having _same content_, Zapper will send the content only once, but generate multiple files for you at arrival.
