import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import br.com.restful.bean.Cliente;
import br.com.restful.bean.Mensagem;


public class ClienteWS {
	private Cliente cliente = new Cliente();
	public static final String SERVER_PORT = "8080";


	public boolean connect(Cliente c){
		cliente = c;
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

			WebResource webResource = client
					.resource("http://localhost:"+ClienteWS.SERVER_PORT+"/Restful/chat/sendmessage/"+cliente.getUser()+"/"+cliente.getPassword()+"/"+msg);

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

	public List<Mensagem> getLsMsgs(int idLastMsg){
		try {

			Client client = Client.create();
			String url = "http://localhost:"+ClienteWS.SERVER_PORT+"/Restful/chat/getmessage/"+cliente.getUser()+"/"+cliente.getPassword()+"/"+idLastMsg;
			
			System.out.println(url);
			WebResource webResource = client
					.resource(url);

			ClientResponse response = webResource.accept("application/json")
					.get(ClientResponse.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}
			String output = response.getEntity(String.class);
			return jsonStringToArray(output);

		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Mensagem>();
		}

	}

	public List<Mensagem> jsonStringToArray(String jsonString){
		System.out.println("JsonString:\n"+jsonString);			
		List<Mensagem> mlsMgs = new ArrayList<Mensagem>();
		JSONArray ja;

		try {
			ja = new JSONArray(jsonString);
			Mensagem m;
			Cliente c;

			for (int i=0 ; i<ja.length() ; i++ ) {
				c = new Cliente();
				m = new Mensagem();
				m.setCorpoMensagem((String) ((JSONObject) ja.get(i)).get("corpoMensagem"));
				m.setId((int) ((JSONObject) ja.get(i)).get("id"));
				GregorianCalendar gc = new GregorianCalendar();
				
				gc.setTimeInMillis((long) ((JSONObject) ja.get(i)).get("hora"));
				m.setHora(gc.getTime());
				
				String user = (String) ((JSONObject)((JSONObject) ja.get(i)).get("cliente")).get("user");
				String password = (String) ((JSONObject)((JSONObject) ja.get(i)).get("cliente")).get("password");
				c.setUser(user);
				c.setPassword(password);
				m.setCliente(c);
				mlsMgs.add(m);
			} 

		}catch(JSONException je){
			System.err.println(je.getMessage());
		}
		return mlsMgs;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente clienteWS) {
		this.cliente = clienteWS;
	}


	public static void main(String[] args) {
		ClienteWS cws = new ClienteWS();
		Cliente c = new Cliente();
		c.setUser("sss");
		c.setPassword("senha");
		cws.setCliente(c);
		//System.out.println("Conectado: "+cws.connect(c));
		//System.out.println("Mensagem enviada: "+cws.sendMessage("Mensagem 3"));
		//System.out.println("Mensagem enviada: "+cws.sendMessage("mensagem 4"));
		//System.out.println(cws.connect(c));
		System.out.println("Mensagens(1) no servidor: "+cws.getLsMsgs(0));
		
	}
}