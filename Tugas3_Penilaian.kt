fun main() {
    println("╔══════════════════════════════════════╗")
    println("║        SISTEM PENILAIAN MAHASISWA    ║")
    println("╚══════════════════════════════════════╝\n")

    print("Masukkan Nama Mahasiswa: ")
    val nama = readLine() ?: "Masukkan Nama WOii"

    val nilaiUTS = inputNilai("Masukkan Nilai UTS (0-100): ")
    val nilaiUAS = inputNilai("Masukkan Nilai UAS (0-100): ")
    val nilaiTugas = inputNilai("Masukkan Nilai Tugas (0-100): ")

    val nilaiAkhir = (nilaiUTS * 0.3) + (nilaiUAS * 0.4) + (nilaiTugas * 0.3)
    val grade = grade(nilaiAkhir)
    val keterangan = keteranganNilai(grade)
    val status = if (nilaiAkhir >= 60) "LULUS" else "TIDAK LULUS"

    println("\n╔══════════════════════════════════════════════════╗")
    println("║                HASIL PENILAIAN                   ║")
    println("╠══════════════════════════════════════════════════╣")
    println("║ Nama        : %-32s ║".format(nama))
    println("║ Nilai UTS   : %-3d (Bobot 30%%)                  ║".format(nilaiUTS))
    println("║ Nilai UAS   : %-3d (Bobot 40%%)                  ║".format(nilaiUAS))
    println("║ Nilai Tugas : %-3d (Bobot 30%%)                  ║".format(nilaiTugas))
    println("╟──────────────────────────────────────────────────╢")
    println("║ Nilai Akhir : %-32.2f ║".format(nilaiAkhir))
    println("║ Grade       : %-32s ║".format(grade))
    println("║ Keterangan  : %-32s ║".format(keterangan))
    println("║ Status      : %-32s ║".format(status))
    println("╚══════════════════════════════════════════════════╝")

    println()
    if (status == "LULUS") {
        println("╔══════════════════════════════════════╗")
        println("║   Selamat! Anda dinyatakan LULUS.    ║")
        println("╚══════════════════════════════════════╝")
    } else {
        println("╔══════════════════════════════════════╗")
        println("║   Maaf, Anda dinyatakan TIDAK LULUS. ║")
        println("╚══════════════════════════════════════╝")
    }

    println("\n╔══════════════════════════════════════╗")
    println("║ Saran                                ║")
    println("╠══════════════════════════════════════╣")
    println("║ %-36s ║".format(getSaran(grade)))
    println("╚══════════════════════════════════════╝")
}

fun inputNilai(prompt: String): Int {
    while (true) {
        print(prompt)
        val input = readLine()?.toIntOrNull()
        if (input != null && input in 0..100) {
            return input
        } else {
            println("Masukin yang bener Woi!")
        }
    }
}

fun grade(nilai: Double): String {
    return when {
        nilai in 85.0..100.0 -> "A"
        nilai in 70.0..84.9  -> "B"
        nilai in 60.0..69.9  -> "C"
        nilai in 50.0..59.9  -> "D"
        else                 -> "E"
    }
}

fun keteranganNilai(grade: String): String {
    return when (grade) {
        "A"  -> "Sangat Baik"
        "B"  -> "Baik"
        "C"  -> "Cukup"
        "D"  -> "Kurang"
        else -> "Sangat Kurang"
    }
}

fun getSaran(grade: String): String {
    return when (grade) {
        "A"  -> "Anak Pinter"
        "B"  -> "Bagus Bro"
        "C"  -> "Lumayan Bro"
        "D"  -> "Mancing Bang"
        else -> "Awokaokawoakawok"
    }
}