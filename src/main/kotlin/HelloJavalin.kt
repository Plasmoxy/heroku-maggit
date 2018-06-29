import io.javalin.Javalin
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

object ResLoader {
	fun read(name: String): String {
		return javaClass.getResource(name).readText()
	}
}

fun main(args: Array<String>) {
	
	val app = Javalin.create()
			.port(getHerokuAssignedPort())
			.enableStaticFiles("public")
			.start()
			
	
	app.get("/") {
		
		println("${it.ip()} GET /")
		
		var initStr = ResLoader.read("index.html")
		
		initStr = initStr.replace("@[time]", SimpleDateFormat("HH:mm:ss").format(Date()))
		
		it.html(initStr)
	}
	
	app.get("/readCyka") {
		it.result(File("a.txt").readText())
	}
	
}

private fun getHerokuAssignedPort(): Int {
	val processBuilder = ProcessBuilder()
	return if (processBuilder.environment()["PORT"] != null) {
		Integer.parseInt(processBuilder.environment()["PORT"])
	} else 7000
}