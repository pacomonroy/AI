import java.util.LinkedList;

public class Actividad {
	int id,tiempo;
	String descripcion;
	LinkedList<Integer> dependencias = new LinkedList<Integer>(); 
	
	
	Actividad (int iden, int t, LinkedList<Integer> dep, String des){
		this.id = iden;
		this.tiempo = t;
		this.dependencias = dep;
		this.descripcion = des;

	}
	
	
	public int getId() {
		return id;
	}

	
	public int getTiempo() {
		return tiempo;
	}
	

	public void getDependencias() {
		System.out.println(dependencias);
	}


	public String getDescripcion() {
		return descripcion;
	}


	
	


	

}
