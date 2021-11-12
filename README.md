# FogComputing-and-Data-Deduplication
A file sharing system inside a Fog Network where deduplication algorithm has been implemented in the server backend.

This was my BE project where we had implemented a file sharing system inside a Fog network. For designing the Fog network we had used a Raspberry Pi as our backend server. The server contained a data deduplication code which was used to find duplicate data at block level and store only those blocks which were unique. The database was MySQL and the administration tool used was HeidiSQL. To access the server we had developed an Android App and data was transfered through a simple socket connection. To maintain security of the data we have implemented a Seed block algorithms and for hashing we have used MD5 algorithm.
