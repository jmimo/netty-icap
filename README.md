ICAP Codec for JBoss Netty
=============

This project enables full ICAP 1.0 Support in combination with JBoss Netty.

http://icap.mimo.ch

Roadmap
------------

### Release 2.0.0

In order to keep up to date with the latest changes at Netty we are starting a 2.0.0 stream which will integrate
with the latest project layout changes at Netty.

### Future releases

In order to stay in sync with the ongoing efforts with Netty and it's Netty 4 refactoring efforts
we plan to adapt our project as a version 2.0 stream to the new Netty 4 stream.

We have implemented basic semantic handlers that will aggregate and separate
messages and bodies for the user. We further plan to build a handler that
encapsulates preview handling form a client perspective.

Another future functionality will be the implementation of the RFC draft: "icap-ext-partial-content"

Contributing
------------

Please go to http://icap.mimo.ch and contact me via the contact form.

License
------------

The project is licensed under the Apache License 2.0
