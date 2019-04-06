package es.upm.etsiinf.bbdd.proyecto.conexion;
import java.util.Scanner;

public class Gestor {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		 boolean seguir = false; 
	     do {
	       System.out.println("\nSeleccione la opci�n que desea ejecutar: ");
	       System.out.println("1. Numero de vinos en la base de datos");
	       System.out.println("2. Vino nombre/ Descrpcion (TABLA)");
	       System.out.println("3. Valoraciones de usuarios de un vino");
	       System.out.println("4. Numero de valoraciones, media usuario y profesional");
	       System.out.println("5. �Estamos abiertos?");
	       System.out.println("6. �Tengo alg�n descuento?");
	       System.out.println("7. Filtrar libros");
	       System.out.println("8. Ordenar libros seg�n criterio");
	       System.out.println("9. Consulta tus marcap�ginas");
	       System.out.println("10. Apagar programa"); 
	       switch (sc.nextInt()){
	         case 1:
	           seguir=true;
	           libreria = entraSale(libreria); 
	           break;
	         case 2:
	           seguir=true;
	           verLibreria(libreria); 
	           break;             
	         case 3:
	           seguir=true;
	           libreria=caracteristica(libreria);
	           break;
	         case 4:
	           seguir=true;
	           contadorGeneros(libreria);
	           break; 
	         case 5:
	           seguir=true;
	           horario();
	           break;
	         case 6:
	           seguir=true;
	           descuento();
	           break;
	         case 7:
	           seguir=true;
	           filtrar(libreria);
	           break;
	         case 8:
	           seguir=true;
	           ordenar(libreria);
	           break;
	         case 9:
	           seguir=true;
	           marcapag(libreria);
	           break;
	         case 10:
	          seguir=false;
	          break; 
	         default: 
	           System.out.println("Opci�n incorrecta, pruebe otra vez\n");
	           seguir=true;    
	       }
	     }while(seguir);

	}

}
