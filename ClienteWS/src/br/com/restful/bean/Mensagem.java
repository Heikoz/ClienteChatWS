package br.com.restful.bean;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Mensagem {
	private String corpoMensagem;
	private Cliente cliente;
	private Date hora;
	private int id;
	
	public Mensagem(){}
	
	public Mensagem(Cliente cliente, String msg){
		this.cliente = cliente;
		this.corpoMensagem = msg; 
		this.hora = new Date();
	}

	public Date getHora() {
		return hora;
	}

	public String getCorpoMensagem() {
		return corpoMensagem;
	}

	public void setCorpoMensagem(String corpoMensagem) {
		this.corpoMensagem = corpoMensagem;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public void setHora(Date hora) {
		this.hora = hora;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(hora);
		String time = gc.get(Calendar.HOUR)+" | "+gc.get(Calendar.MINUTE)+" | "+gc.get(Calendar.SECOND);
		
		return id+"| "+ time +": \""+cliente.getUser()+"= "+corpoMensagem;
	}
	
}
