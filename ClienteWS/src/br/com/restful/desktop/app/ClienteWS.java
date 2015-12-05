package br.com.restful.desktop.app;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.JTextArea;
import javax.ws.rs.core.UriBuilder;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import windows.MainWindow;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.uri.UriBuilderImpl;

import br.com.restful.bean.Cliente;
import br.com.restful.bean.Mensagem;


public class ClienteWS {
	private Cliente cliente = new Cliente();
	public static final String SERVER_PORT = "8080";
	private Thread t;
	private RequestMessages rm;
	private JTextArea txtArea;
	private MainWindow mw;
	
	public ClienteWS(JTextArea txtArea, MainWindow mw){	
		this.txtArea = txtArea;
		this.mw = mw;
	}

	public void desconnect(){
		rm.parar();
	}
	
	public boolean connect(Cliente c){
		cliente = c;
		rm = new RequestMessages(c, txtArea, mw);
		t = new Thread(rm);
		t.start();
		try {

			Client client = Client.create();

			WebResource webResource = client
					.resource("http://localhost:"+ClienteWS.SERVER_PORT+"/Restful/chat/"+c.getUser()+"/"+c.getPassword());

			ClientResponse response = webResource.accept("application/json")
					.get(ClientResponse.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}

			String output = response.getEntity(String.class);
			return Boolean.parseBoolean(output);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public String sendMessage(String msg){
		try {

			Client client = Client.create();
			//msg = processMessage(msg);			
			UriBuilder uriBuilder = new UriBuilderImpl()
	            .scheme("http")
	            .host("localhost").port(8080)
	            .path("/Restful/chat/sendmessage/"+cliente.getUser()+"/"+cliente.getPassword()+"/"+msg);
						
			URI uri = uriBuilder.build();
			WebResource webResource = client.resource(uri.toString());
			System.out.println(uri.toString());

			ClientResponse response = webResource.accept("application/json")
					.get(ClientResponse.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}

			String output = response.getEntity(String.class);
			return output;

		} catch (Exception e) {
			e.printStackTrace();
			return "Erro ao tentar enviar a mensagem.";
		}

	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente clienteWS) {
		this.cliente = clienteWS;
	}


	public static void main(String[] args) {
//		ClienteWS cws = new ClienteWS();
//		Cliente c = new Cliente();
//		c.setUser("sss");
//		c.setPassword("senha");
//		cws.setCliente(c);
//		//System.out.println("Conectado: "+cws.connect(c));
		//System.out.println("Mensagem enviada: "+cws.sendMessage("Mensagem 3"));
		//System.out.println("Mensagem enviada: "+cws.sendMessage("mensagem 4"));
		//System.out.println(cws.connect(c));
		//System.out.println("Mensagens(1) no servidor: "+cws.getLsMsgs(0));
		
	}
		
}