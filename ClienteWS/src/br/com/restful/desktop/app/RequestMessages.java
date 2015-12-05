package br.com.restful.desktop.app;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.JTextArea;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import windows.MainWindow;
import br.com.restful.bean.Cliente;
import br.com.restful.bean.Mensagem;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class RequestMessages implements Runnable {
	private Cliente cliente;
	private JTextArea txtArea;
	private MainWindow mw;
	private boolean pararThread = false;
	private List<Mensagem> msgs = new ArrayList<Mensagem>();
	
	public RequestMessages(Cliente c, JTextArea txtArea, MainWindow mw) {
		cliente = c;
		this.txtArea = txtArea;
		this.mw = mw;
	}
	
	  
    public void parar() {  
        pararThread = true;  
    }  
	
	public List<Mensagem> getLsMsgs(int idLastMsg){
		try {

			Client client = Client.create();
			String url = "http://localhost:"+ClienteWS.SERVER_PORT+"/Restful/chat/getmessage/"+cliente.getUser()+"/"+cliente.getPassword()+"/"+idLastMsg;
			
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

	@Override
	public void run() {

		while(!pararThread){
			System.out.println("loop");
			
			for (Mensagem m : getLsMsgs(getLastMessageId())) {
				msgs.add(m);
				if (!m.getCliente().getUser().equals(cliente.getUser())){
					txtArea.append(m.getCliente().getUser()+": "+m.getCorpoMensagem()+"\n");
				}
			}
			
			mw.revalidate();
			mw.repaint();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}   
		}
		
	}


	private int getLastMessageId() {
		int majorId = 0;
		for (Mensagem m : msgs) {
			if (m.getId() > majorId)
				majorId = m.getId();
		}
		return majorId;
	}


}
