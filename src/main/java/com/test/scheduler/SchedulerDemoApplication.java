package com.test.scheduler;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@SpringBootApplication
public class SchedulerDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchedulerDemoApplication.class, args);
	}

	@Scheduled(fixedDelayString = "PT2H", initialDelay = 1000L)
	public void job() throws InterruptedException, ParseException {
		System.out.println("ESCANEO DE DIRECTORIO AL ::" + new Date());

		// Fecha de referencia
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date fechaReferencia = dateFormat.parse("2023-09-16");

		//Directorio
		String sCarpAct = System.getProperty("user.dir");
		String path = sCarpAct + "/archivos-que-el-gobierno-no-quiere-que-veas";

		borrarArchivosAntiguos(path, fechaReferencia);
		
		Thread.sleep(1000L);

	}
	
	public void borrarArchivosAntiguos(String pathDirectorio, Date fechaReferencia) {		
		File carpeta = new File(pathDirectorio);
		String[] listado = carpeta.list();

		if (listado == null || listado.length == 0) {
			System.out.println("No hay elementos dentro de la carpeta");
			return;
		} else {
			for (int i = 0; i < listado.length; i++) {
				File fichero = new File(pathDirectorio + "/" + listado[i]);
				if (!fichero.isDirectory()) {
					if (fichero.exists()) {
						
						long ms = fichero.lastModified();
						Date d = new Date(ms);

						if (d.before(fechaReferencia)) {
							System.out.println(fichero.getName() + " es antiguo");

							// ELiminar el fichero antiguo a la fecha de referencia
							if (fichero.delete())
								System.out.println("El fichero " + fichero.getName() 
										+ " ha sido borrado satisfactoriamente");
							else
								System.out.println("El fichero " + fichero.getName() 
										+ " no puede ser borrado");
						}
					} else {
						System.out.println(fichero.getName() + " no existe.");
					}
				}
			}
		}

	}

}
