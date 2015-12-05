package teste;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import br.com.restful.bean.Cliente;
import br.com.restful.bean.Mensagem;
import br.com.restful.logic.IPAddressValidator;

public class Teste {	
	
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
	
}