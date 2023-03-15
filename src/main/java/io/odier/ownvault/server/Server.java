package io.odier.ownvault.server;

import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.lang.reflect.*;

public class Server
{
	/*----------------------------------------------------------------------------------------------------------------*/

	private static final String ENDPOINT = "https://ownvault.odier.io/repo/latest/OwnVaultAPI.jar";

	/*----------------------------------------------------------------------------------------------------------------*/

	private String getPath() throws URISyntaxException
	{
		File file = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());

		return file.isFile() ? file.getParent()		// JAR bundle
		                     : file.getPath()		// classes
		;
	}

	/*----------------------------------------------------------------------------------------------------------------*/

	private File download(File dst, URL url) throws IOException
	{
		try
		{
			try(InputStream inputStream = url.openStream())
			{
				try(FileOutputStream outputStream = new FileOutputStream(dst))
				{
					outputStream.getChannel().transferFrom(Channels.newChannel(inputStream), 0, Long.MAX_VALUE);
				}
			}
		}
		catch(Exception e)
		{
			throw new IOException(String.format("Error downloading '%s'", url.toString()));
		}

		return dst;
	}

	/*----------------------------------------------------------------------------------------------------------------*/

	public int run(String[] args)
	{
		try
		{
			/*--------------------------------------------------------------------------------------------------------*/
			/* DOWNLOAD THE OWNVAULT SERVER                                                                           */
			/*--------------------------------------------------------------------------------------------------------*/

			File file = download(new File(this.getPath(), "OwnVaultAPI.jar"), new URL(ENDPOINT));

			/*--------------------------------------------------------------------------------------------------------*/
			/* LOAD THE OWNVAULT SERVER                                                                               */
			/*--------------------------------------------------------------------------------------------------------*/

			ClassLoader classLoader = new URLClassLoader(new URL[] {file.toURI().toURL()}, this.getClass().getClassLoader());

			/*--------------------------------------------------------------------------------------------------------*/
			/* RUN THE OWNVAULT SERVER                                                                                */
			/*--------------------------------------------------------------------------------------------------------*/

			Class<?> Server = Class.forName("io.odier.ownvault.api.Server", true, classLoader);

			/*--------------------------------------------------------------------------------------------------------*/

			Object server = Server.getDeclaredConstructor().newInstance();

			Method method = Server.getMethod("run", String[].class);

			return (int) method.invoke(server, new Object[] {args});

			/*--------------------------------------------------------------------------------------------------------*/
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());

			return 1;
		}
	}

	/*----------------------------------------------------------------------------------------------------------------*/

	public static void main(String[] args)
	{
		System.exit(new Server().run(args));
	}

	/*----------------------------------------------------------------------------------------------------------------*/
}
