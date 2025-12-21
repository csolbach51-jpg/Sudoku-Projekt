plugins {
   id 'java'
}

// Hilfsvariable, um den Zugriff sauber zu definieren
val mainSourceSet = sourceSets.main.get()

tasks.register<Jar>("clientJar") {
    archiveClassifier.set("client")
    manifest {
        attributes["Main-Class"] = "org.solbach.sudoku" // Pfad anpassen!
    }
    // Hier nutzen wir direkt die Files der Main-SourceSet
    from(mainSourceSet.output)
}

tasks.register<Jar>("serverJar") {
    archiveClassifier.set("server")
    manifest {
        attributes["Main-Class"] = "org.solbach.sudoku" // Pfad anpassen!
    }
    from(mainSourceSet.output)
}
