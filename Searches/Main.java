import java.util.Scanner;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Main {
	
	static int size;
	//static ArrayList<ArrayList<Integer>> poblacion = new ArrayList<ArrayList<Integer>>();

	
	
	//POBLACION INICIAL USANDO DFS
	public static ArrayList<ArrayList<Integer>> dfs(int first, int last, int [][] mat, boolean[] flag, ArrayList<Integer> list, ArrayList<ArrayList<Integer>> poblacion) {
	    
		
		ArrayList<Integer> aux = new ArrayList<Integer>();
		//nodo inicial se agrega
		
        list.add(first); //nodo inicial se agrega
        size++;
        //imprimeMat(mat);
        
        
        
        flag[first] = true; //marcar nodo como visitado
        
        if (first == last) { 
        	
            for (Integer node : list) {
                aux.add(node); //agregar nodo a lista auxiliar
                
                
                //path.add(node);
                
            }
           
            poblacion.add(aux); //agregar lista auxiliar a poblacion
         
        }
        
        
        for (int I = 1; I <= last; I++) {
       
        //si no hay vertices entre first y el nodo actual
            if (mat[first][I] == 1) {
				
                if (flag[I] == false) { //si el nodo actual no se ha visitado
					
                	//Recursividad sobre nodo actual
                    dfs(I, last,mat,flag,list,poblacion);
                    
                    
                    //Marcar el nodo actual como no visitado para buscar otro camino al destino
                    flag[I] = false;
                    
                    //size of list reduced by 1
                    //as the node is marked unvisited
                    //hence removed from the list
                    
                    //el tamaño de la lista se reduce 1 mientras el nodo se marca como no visitado
                    size--;
                    
                    //eliminar el nodo en la posicion (size)
                    list.remove(size);
                }
            }
        }
    
        return poblacion;
    }

	

	 private static void invierteMat(int[][] mat) { 
	        for(int i=0;i<mat.length/2;i++) { 
	            for(int j=0;j<mat.length;j++) { 
	                int temp = mat[i][j]; 
	                mat[i][j] = mat[mat.length-i-1][mat.length-j-1]; 
	                mat[mat.length-i-1][mat.length-j-1] = temp; 
	            } 
	        } 
	    } 
	public static void imprimeMat(int[][] mat) {
		for (int i = 0; i < mat.length; i++) {
			  
            // Loop through all elements of current row 
            for (int j = 0; j < mat[i].length; j++) { 
                System.out.print(mat[i][j] + " "); 
            }
            System.out.println();
		}
		
    } 
	/*
	public static void matSearch(LinkedList<Actividad> cam, int[][] mat) {
		
		for (int i = 0; i <=cam.size(); i++) {
			//System.out.println(cam.get(i).getId());

			if(cam.get(i+1).dependencias.contains(cam.get(i).getId())) {
				for (int j = 0; j < cam.get(i+1).dependencias.size(); j++) {
					//System.out.println();
					mat[i+1][cam.get(i+1).dependencias.get(j)] = 1;
					System.out.println(cam.get(i+1).dependencias.get(j));
					
				}
				//System.out.println("EXITO -Dependencia en la actividad: "+cam.get(i+1).getId());
				
			}
		}
	}
*/
	
	public static int tiempoTotal(LinkedList<Actividad> camino){
		int total=0;
		for (int i = 0; i < camino.size(); i++) {
			total += camino.get(i).tiempo;
		}
		
		return total;
	}
	
	/*
	public static int funcionAptitud(LinkedList<Actividad> cam) {
		int puntos=0;
		
		puntos+=tiempoTotal(cam);
		
		for (int i = 0; i < cam.size()-1; i++) {
			
			if(cam.get(i+1).dependencias.contains(cam.get(i).getId())) {
				System.out.println("EXITO -Dependencia en la actividad: "+cam.get(i+1).getId());
				puntos +=100;
			}else {
				puntos+=50;
			}
			
		}
		if(cam.getFirst().getId()==0){
			System.out.println("EXITO - Primer nodo ");
			puntos += 200;
		}if (cam.getLast().getId() == cam.size()-1) {
			System.out.println("EXITO - Último nodo ");
			puntos +=200;
		}
		return puntos;
		
	}
	*/
	
	
	//FUNCION DE APTITUD
	public static int fA(ArrayList<Integer> camino, Actividad act[] ) {
		int puntos=0;
		for (int i = 0; i < camino.size(); i++) {
			puntos+= act[camino.get(i)].getTiempo();
			//System.out.println(puntos);
		}
		return puntos;
	}
	
	//SELECCION (ELITISMO)
	public static ArrayList<ArrayList<Integer>>  seleccion (ArrayList<Integer> puntos,ArrayList<ArrayList<Integer>> poblacion ) {
		
		int s1 = Integer.MIN_VALUE, s2 = Integer.MIN_VALUE;
		ArrayList<ArrayList<Integer>> sobrevivientes = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < puntos.size(); i++) {
			if(puntos.get(i)>s1) {
				s2 = s1;
				s1 =i;
				
			}else if(puntos.get(i)>s2) {
				s2 = i;
			}
			
		}
		System.out.println(s1);
		sobrevivientes.add(poblacion.get(s1));
		sobrevivientes.add(poblacion.get(s2));
		
		
		return sobrevivientes;
	}
	
	public static ArrayList<ArrayList<Integer>>  seleccion2 (ArrayList<Integer> puntos,ArrayList<ArrayList<Integer>> poblacion ) {
		
		int res=0,max=0;
		ArrayList<ArrayList<Integer>> sobrevivientes = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < puntos.size(); i++) {
			if(puntos.get(i)>max) {
				res =i;
				max = puntos.get(i);
				
			}
			
		}
		//System.out.println(res);
		System.out.println("\nDURACIÓN MÍNIMA: "+puntos.get(res)+" días");
		sobrevivientes.add(poblacion.get(res));
		//sobrevivientes.add(poblacion.get(res));
		
		
		return sobrevivientes;
	}
	
	//CRUCE (SINGLE POINT CROSSING)
	public static ArrayList<ArrayList<Integer>> cruce(ArrayList<ArrayList<Integer>>  sobrevivientes) {
		Random r = new Random();
		ArrayList<ArrayList<Integer>> nuevaPoblacion = new ArrayList<ArrayList<Integer>>();
		Object[] padre = sobrevivientes.get(0).toArray();
		Object[] madre = sobrevivientes.get(1).toArray();
		int puntoCruce = r.nextInt()*padre.length;
		//SINGLE POINT CROSSING
		for (int i = 0; i < padre.length; i++) {
			if(i<puntoCruce) {
				nuevaPoblacion.get(i).add(sobrevivientes.get(0).get(i));
			}
			else {
				nuevaPoblacion.get(i).add(sobrevivientes.get(1).get(i));
			}
		}
		
		return nuevaPoblacion;
	}
	
	
	

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		BufferedReader br = new BufferedReader (new FileReader("/Users/paco/eclipse-workspace/Tesina/src/test3.txt"));
		Random r = new Random();
		int n, dep,ndep, t;
		int rn, rdep, rt;
		int cont = 100;
		long tiempo = System.nanoTime(); 
		
		
		
		
		 
		//int[] aux = new int[1];
		
		
		System.out.println("Ingrese el # de actividades");
		//n = sc.nextInt();
		n= Integer.parseInt(br.readLine());
		//rn = r.nextInt(10)+1;
		
		//Actividad act[] = new Actividad [rn];
		Actividad act[] = new Actividad [n+1];
		int mat[][] = new int[n*2][n*2]; 
		int mat2[][] = new int[n*2][n*2];
		int matDep[][] = new int[n*2][n*2]; 
		int matPrueba[][] = new int[8][8];  
		boolean[] flag = new boolean[n + 1];
		Arrays.fill(flag, false);
		
		LinkedList<Actividad> camino = new LinkedList<Actividad>(); 

		for(int i= 1; i<=n; i++) {
			LinkedList<Integer> dependencias = new LinkedList<Integer>();
			String descripcion;
			System.out.println("Ingrese el tiempo de la actividad: "+i);
			t = Integer.parseInt(br.readLine());
			System.out.println("Ingrese la descripcion");
			descripcion = br.readLine();
			
			//t = sc.nextInt();
			//rt = r.nextInt(100);
			
			System.out.println("Ingrese el # de dependencias en la actividad: "+i);
			dep = Integer.parseInt(br.readLine());
			//dep = sc.nextInt();
			//rdep = r.nextInt(2)+1;
			if (dep>0) {
				
				int[] aux = new int[dep];
				for (int j = 0; j < dep; j++) {
					System.out.println("Ingrese la dependencia #"+j);
					ndep = Integer.parseInt(br.readLine());
					//ndep = sc.nextInt();
					//aux[j+1] = ndep;
					//aux[j] = r.nextInt(rn);
					dependencias.add(ndep);
					//dependencias.add(r.nextInt(rn));
					mat[i+(n-2)][ndep+(n-2)] = 1;
				}
				act[i] = new Actividad(i,t,dependencias,descripcion);
				camino.add(act[i]);
				System.out.println("Actividad: "+act[i].getId());
				System.out.println("Tiempo: "+act[i].getTiempo());
				System.out.println("Dependencia: "+dependencias);
				System.out.println("#Dep: "+dependencias.size());
				System.out.println("#Descripcion: "+descripcion);
				
				
			}else if(dep==0) {
				int[] aux = new int[0];
				act[i] = new Actividad(i,t,dependencias,descripcion);
				camino.add(act[i]);
				System.out.println("Actividad: "+act[i].getId());
				System.out.println("Tiempo: "+act[i].getTiempo());
				System.out.println("Dependencia: "+dependencias);
				System.out.println("#Dep: "+dependencias.size());
				System.out.println("#Descripcion: "+descripcion);
				
			}

		}
		
		Camino camino2 = new Camino(1,act,0);
		
		System.out.println("Num de actividades: "+camino.size());
		//System.out.println("Tiempo total en Camino 1: "+camino2.getTiempoTotal());
		//System.out.println("Las dependencias de la act0: "+camino.getActDepTotal(2));
		//System.out.println("CAMINO SIZE:"+camino.size());
		//System.out.println("TIEMPO TOTAL: "+tiempoTotal(camino));
		//System.out.println("SCORE: "+funcionAptitud(camino));
		//imprimeMat(mat);
		//System.out.println("-------------------");
		//invierteMat(mat);
		System.out.println("SIZE MAT: "+mat.length);
		
	/*
		for (int i = 0; i < camino.size(); i++) {
			camino.remove(i);
		}
		
		*/
		//imprimeMat(mat);
		//System.out.println("-----------------------------------------------");

		//invierteMat(mat);
		//imprimeMat(mat);
		//imprimeMat(mat);
		
		
		
		invierteMat(mat);
		imprimeMat(mat);
		System.out.println("-----------------------------------------------");

		//matSearch(camino,mat2);
		System.out.println("MATRIZ DE DEPENDENCIAS");
		//imprimeMat(mat2);
		System.out.println("-----------------------------------------------");
		/*
		matPrueba[1][2] = 1;
		matPrueba[1][3] = 1;
		matPrueba[2][4] = 1;
		matPrueba[3][4] = 1;
		System.out.println("-----------------------------------------------");
		imprimeMat(matPrueba);
		//invierteMat(matPrueba);
		
		*/
		
		ArrayList<ArrayList<Integer>> poblacion = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> list = new ArrayList<Integer>();
		ArrayList<Integer> individuo = new ArrayList<Integer>();
		ArrayList<Integer> puntos = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>>  sobrevivientes = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> nuevaPoblacion = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> nuevoCamino = new ArrayList<Integer>();
		int contador=5;
		
		
		//POBLACION INICIAL
		
		poblacion = dfs(1,camino.getLast().id,mat,flag,list,poblacion);

		
        for (int i = 0; i < poblacion.size(); i++) { 
        	
            for (int j = 0; j < poblacion.get(i).size(); j++) { 
                System.out.print(poblacion.get(i).get(j) + " "); 
            } 
            System.out.println(); 
        }
        
        //FUNCION DE APTITUD
        for (int i = 0; i < poblacion.size(); i++) {
        	individuo = poblacion.get(i);
        	puntos.add(fA(individuo,act));
        	
		}
        
        
        for (int i = 0; i < puntos.size(); i++) {
    	   System.out.println("PUNTOS DE INDIVIDUO: "+(i+1)+" -> "+puntos.get(i));
	}
        long tiempoEnd = System.nanoTime(); //END TIMING 1
        long duracion = (tiempoEnd - tiempo); //TIMING 1 DURATION
        double segundos = (double)duracion / 1_000_000_000.0;
        
        System.out.println("---------------------------------------------------");
        //SELECCION
        sobrevivientes = seleccion2(puntos,poblacion);
        //sobrevivientes = seleccion(puntos,poblacion);
        puntos.clear(); //Vaciar puntos
        
        System.out.print("\nCAMINO CRÍTICO: ");
        for (int i = 0; i < sobrevivientes.size(); i++) { 
        	
            for (int j = 0; j < sobrevivientes.get(i).size(); j++) {
            	if(j==sobrevivientes.get(i).size()-1) {
            		System.out.print(sobrevivientes.get(i).get(j) + " "); 
            	}else {
            		System.out.print(sobrevivientes.get(i).get(j) + "->"); 
            	}
                
            } 
            System.out.println(); 
        }
       
        System.out.println("\nACTIVIDADES CRÍTICAS:");
        for (int i = 0; i < sobrevivientes.size(); i++) { 
        	
            for (int j = 0; j < sobrevivientes.get(i).size(); j++) {
            	if(j==sobrevivientes.get(i).size()-1) {
            		System.out.print(sobrevivientes.get(i).get(j) + " "); 
            	}else {
            		System.out.print(sobrevivientes.get(i).get(j) + ","); 
            	}
                
            } 
            System.out.println(); 
        }
        
        System.out.println("Tiempo de ejecución: " + segundos +" segundos");
        
        System.out.println("---------------------------------------------------");
        System.out.println("ACTIVIDADES CRÍTICAS: \n");
        for (int i = 0; i < sobrevivientes.size(); i++) { 
        	
            for (int j = 0; j < sobrevivientes.get(i).size(); j++) {
            	if(j==sobrevivientes.get(i).size()-1) {
            		System.out.print((j+1)+".- "+act[sobrevivientes.get(i).get(j)].descripcion); 
            	}else {
            		System.out.print((j+1)+".- "+act[sobrevivientes.get(i).get(j)].descripcion+"\n"); 
            	}
                
            } 
            System.out.println(); 
        }

        //CRUCE
        //nuevaPoblacion = cruce()
        nuevaPoblacion = sobrevivientes;
        
        //FUNCION DE APTITUD
        for (int i = 0; i < nuevaPoblacion.size(); i++) {
        	nuevoCamino = nuevaPoblacion.get(i);
        	puntos.add(fA(nuevoCamino,act));
        	
		}
        poblacion.clear();
        individuo.clear();
        puntos.clear();
        sobrevivientes.clear();
        
        poblacion = nuevaPoblacion;
        nuevaPoblacion.clear();
        nuevoCamino.clear();
        
        // SALIR DEL CICLO

        
        
		
		
		
		
		
	
		
		

		
		

	}

}
