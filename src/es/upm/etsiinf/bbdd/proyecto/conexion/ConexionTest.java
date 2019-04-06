package es.upm.etsiinf.bbdd.proyecto.conexion;

public class ConexionTest {

	public static void main(String[] args) {
	
		Conexion.init("localhost", "root", "CRC175psa", "cata_vinos");
		//String fila[]=Conexion.insertInstructionFromCSV2("\"0\";\"7\";\"2014-02-18 21:05:38\";\"87.00\";NULL");
		//System.out.println(fila[4].equals("NULL"));
		
		
		Conexion.insertVinos2("C:\\Users\\Alejandro\\Desktop\\Proyecto\\wine_user_review.data.csv", "wine_user_review");
		//Conexion.insertVinos2("C:\\Users\\Alejandro\\Desktop\\Proyecto\\wine_scoring_guide.data.csv", "wine_scoring_guide");
		//System.out.println(Conexion.numVinos());
		//System.out.println(Conexion.vinosNombreDescr("Blackberry and raspberry aromas show a typical Navarran whiff of green herbs and, in this case, horseradish. In the mouth, this is fairly full bodied, with tomatoey acidity. Spicy, herbal flavors complement dark plum fruit, while the finish is fresh but grabby."));
		//System.out.println(Conexion.valoracionVinosPRO(179));
		/**Wine[] vinos  = Conexion.vinosNombreDescr("Souverain 2010 Chardonnay (North Coast)");
		for(Wine v:vinos)
			System.out.println(v);
		User[] usuarios = Conexion.usersVal(179);
		for (User u: usuarios)
			System.out.println(u);
		**/
		//System.out.println(Conexion.usuariosMinDesvPro());
	}

}
