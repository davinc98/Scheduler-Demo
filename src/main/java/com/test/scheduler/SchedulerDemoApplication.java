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

		File carpeta = new File(path);
		String[] listado = carpeta.list();

		if (listado == null || listado.length == 0) {
			System.out.println("No hay elementos dentro de la carpeta actual");
			return;
		} else {
			System.out.println("La carpeta actual contiene " + listado.length + " elementos.");

			for (int i = 0; i < listado.length; i++) {
				File fichero = new File(path + "/" + listado[i]);

				if (!fichero.isDirectory()) {
					if (fichero.exists()) {

						long ms = fichero.lastModified();
						Date d = new Date(ms);
						Calendar c = new GregorianCalendar();
						c.setTime(d);

						String dia = Integer.toString(c.get(Calendar.DATE));
						String mes = Integer.toString(c.get(Calendar.MONTH));
						String ann = Integer.toString(c.get(Calendar.YEAR));
						String hor = Integer.toString(c.get(Calendar.HOUR_OF_DAY));
						String min = Integer.toString(c.get(Calendar.MINUTE));
						String seg = Integer.toString(c.get(Calendar.SECOND));

						System.out.println(fichero.getName() + "--> " + dia + "/" + mes + "/" + ann +
								"   " + hor + ":"+ min + ":" + seg);

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

		Thread.sleep(1000L);

	}

}
