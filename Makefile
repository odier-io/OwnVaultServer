all:
	mvn package

	cp ./target/OwnVaultServer-jar-with-dependencies.jar ./container/OwnVaultServer.jar
