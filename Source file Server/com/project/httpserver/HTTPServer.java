package com.project.httpserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

public class HTTPServer extends MyHTTP {
	
	private final String HEADER = "<center><img src=\"/HTTPServer/files/images/header.png\" align=\"center\" alt=\"header\" width=\"100%\"></center>";
	
	public HTTPServer() {
		super(8088);
	}

	@Override public Response serve(IHTTPSession session) {
		Method method = session.getMethod();
		String uri = session.getUri();
		System.out.println(method + " '" + uri + "' ");
		String[] uriArray = uri.split("/");

		if(uri.equalsIgnoreCase("/HTTPServer/") || uri.equalsIgnoreCase("/HTTPServer") || uri.equalsIgnoreCase("/HTTPServer/index.html") || uri.equalsIgnoreCase("/HTTPServer/index.htm") || uri.equalsIgnoreCase("/HTTPServer/logout"))
		{
			String msg = "<html>" +
					"<head> 	<link rel=\"stylesheet\"  href=\"/HTTPServer/files/css/jquery.mobile-1.2.0.css\" />" +
					"<script src=\"/HTTPServer/files/js/jquery.js\"></script>"+
					"<script src=\"/HTTPServer/files/js/jquery.mobile.min.js\"></script>"+
					"<body>" +
					"<div data-role=\"page\" data-theme=\"b\">" +
					HEADER+
					"<div data-role=\"content\" data-theme=\"b\" id=\"content\">"+
					"<div id=\"SelectClientType\" align=\"center\">"+
					"<form name=\"frmNewClient\" action=\"/HTTPServer/validateLogin\" method=\"GET\">"+
					"<table border=\"0\"> 	   " +
					"<tr>"+
					"<td>User Name:</td>"+
					"<td><input type=\"text\" id=\"txtUserName\" name=\"username\" placeholder=\"User Name\" required=\"required\"/></td>"+
					"</tr>"+
					"<tr>"+
					"<td>Password:</td>"+
					"<td><input type=\"password\" id=\"txtPassword\" name=\"password\" placeholder=\"Password\" required=\"required\"/></td>"+
					"</tr>"+
					"<tr>" +
					"<td>"+

						"<input type=\"submit\" id=\"btnSubmit\" value=\"Submit\"/></td>"+
						"<td><input type=\"button\" id=\"btnCancel\" value=\"Cancel\"/></td>"+
						"</tr>"+
						"</table>"+
						"</form>"+
						"</div>"+
						"</div>"+
						"</div>"+
						"</body>"+
						"</html>";
			
			return new MyHTTP.Response(msg);
		}
		
		else if(uri.equalsIgnoreCase("/HTTPServer/validateLogin"))
		{
			Map<String, String> parms = session.getParms();
			String msg = "";
			if(parms.get("username").equalsIgnoreCase("admin") && parms.get("password").equalsIgnoreCase("admin"))
			{
				int fileCount=StatsCalculator.GetTotalfileCount();
				float totalSize=StatsCalculator.TotalSizeNeeded();
				float actualSize=StatsCalculator.acutalSizeCounsumed();
				//=((totalSize/actualSize)/2)*100;
				float dedupRate = ((totalSize-actualSize)/totalSize)*100;
				System.out.println("fileCount:"+fileCount+" totalSize:"+totalSize+" actualSize:"+actualSize);
				msg = "<html>" +
						"<head> 	<link rel=\"stylesheet\"  href=\"/HTTPServer/files/css/jquery.mobile-1.2.0.css\" />" +
						"<script src=\"/HTTPServer/files/js/jquery.js\"></script>"+
						"<script src=\"/HTTPServer/files/js/jquery.mobile.min.js\"></script>"+
						"<body>" +
						"<div data-role=\"page\" data-theme=\"b\">" +
						HEADER+
						"<div id=\"SelectClientType\" align=\"center\"><Table border=\"1\">"+
						"<tr><td>Total File uploaded: </td><td>" +fileCount+" </td></tr>"+
						"<tr><td>Total Size needed:</td><td>"	+totalSize+" Bytes </td></tr>"+
						"<tr><td>Actual Size consumed:</td><td>"	+actualSize+" Bytes</td></tr>"+
						"<tr><td>Dedplication Rate:</td><td>"	+dedupRate+" % </td></tr></table>"+
						"</div>"+
						"</div>"+
						"</body>"+
						"</html>";
			}

			else
			{
				msg = "<html>" +
						"<head> 	<link rel=\"stylesheet\"  href=\"/HTTPServer/files/css/jquery.mobile-1.2.0.css\" />" +
						"<script src=\"/HTTPServer/files/js/jquery.js\"></script>"+
						"<script src=\"/HTTPServer/files/js/jquery.mobile.min.js\"></script>"+
						"<body>" +
						"<div data-role=\"page\" data-theme=\"b\">" +
						HEADER+
						"<div data-role=\"content\" data-theme=\"b\" id=\"content\">"+
						"<div id=\"SelectClientType\" align=\"center\">"+
						"<form name=\"frmNewClient\" action=\"/HTTPServer/validateLogin\" method=\"GET\">"+
						"<table border=\"0\"> 	   " +
						"<tr>"+
						"<td>User Name:</td>"+
						"<td><input type=\"text\" id=\"txtUserName\" name=\"username\" placeholder=\"User Name\" required=\"required\"/></td>"+
						"</tr>"+
						"<tr>"+
						"<td>Password:</td>"+
						"<td><input type=\"password\" id=\"txtPassword\" name=\"password\" placeholder=\"Password\" required=\"required\"/></td>"+
						"</tr>"+
						"<tr>" +
						"<td>"+

							"<input type=\"submit\" id=\"btnSubmit\" value=\"Submit\"/></td>"+
							"<td><input type=\"button\" id=\"btnCancel\" value=\"Cancel\"/></td>"+
							"</tr>"+
							"</table>"+
							"</form>"+
							"</div>"+
							"</div>"+
							"</div>"+
							"</body>"+
							"</html>";
			}
			return new MyHTTP.Response(msg);
		}
		
		else if(uri.startsWith("/HTTPServer/files"))
		{
			System.out.println("hello :" +uri.substring(1));
			InputStream fin = null;
			try {
				fin =  new FileInputStream(new File(uri.substring(1)));
			} catch (Exception ex) {
				ex.printStackTrace();
			} 
			return new Response(Response.Status.OK,MIME_PLAINTEXT,fin);
		}
		
		else
		{
			String msg = "<html><body><h1>Hello server</h1>\n";
						msg += "</body></html>\n";

			return new MyHTTP.Response(msg);
		}
	}
}
