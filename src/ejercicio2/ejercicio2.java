package ejercicio2;

public class ejercicio2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int numeroIf = -5;
		
		if (numeroIf > 0) {
			System.out.println("Numero positivo");
		}
		else if(numeroIf < 0) {
			System.out.println("Numero negativo");
		}
		else {
			System.out.println("El numero es 0");
		}
		
		int numeroWhile = 0;
		
		while(numeroWhile < 3) {
			numeroWhile ++;
			System.out.println(numeroWhile);
		}
		
		do {
			System.out.println("Entré al blucle do While");
		}while(numeroWhile < 3);
		
		for (int numeroFor = 0; numeroFor <= 3; numeroFor++) {
			System.out.println(numeroFor);
		}
		
		String estacion = "OTOÑO";
		
		switch(estacion) {
		case "VERANO":
			System.out.println("Es verano");
			break;
		case "INVIERNO":
			System.out.println("Es invierno");
			break;
		case "OTOÑO":
			System.out.println("Es otoño");
			break;
		default:
			System.out.println(estacion);
		}
		
		
	}

}
