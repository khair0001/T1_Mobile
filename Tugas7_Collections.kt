data class NilaiMahasiswa(
    val nim: String,
    val nama: String,
    val mataKuliah: String,
    val nilai: Int
)

fun main() {
    val dataMahasiswa = listOf(
        NilaiMahasiswa("F1D023001", "Khair", "Pemrograman Mobile", 100),
        NilaiMahasiswa("F1D023101", "Nartoh", "Pemrograman Mobile", 92),
        NilaiMahasiswa("F1D023102", "Pria Solo", "Pemrograman Mobile", 68),
        NilaiMahasiswa("F1D023103", "Pria Sawit", "Pemrograman Mobile", 45),
        NilaiMahasiswa("F1D023104", "Bahlil", "Pemrograman Mobile", 77),
        NilaiMahasiswa("F1D023105", "Pigai", "Pemrograman Mobile", 55),
        NilaiMahasiswa("F1D023106", "Gibrun", "Pemrograman Mobile", 20),
        NilaiMahasiswa("F1D023107", "Levi", "Pemrograman Mobile", 73),
        NilaiMahasiswa("F1D023108", "Pain", "Pemrograman Mobile", 62),
        NilaiMahasiswa("F1D023109", "Konan", "Pemrograman Mobile", 49),
        NilaiMahasiswa("F1D023110", "Umam", "Pemrograman Mobile", 85),
        NilaiMahasiswa("F1D023111", "Itachi", "Pemrograman Mobile", 92),
    )

    tampilkanSemuaData(dataMahasiswa)
    status(dataMahasiswa)
    statistik(dataMahasiswa)
    shorting(dataMahasiswa)
    gradeTampilan(dataMahasiswa)
    search(dataMahasiswa, "an")
}

fun getGrade(nilai: Int): String {
    return when {
        nilai in 85..100 -> "A"
        nilai in 70..84 -> "B"
        nilai in 60..69 -> "C"
        nilai in 50..59 -> "D"
        else -> "E"
    }
}

fun tampilkanSemuaData(list: List<NilaiMahasiswa>) {

    println("\n╔═══════════════════════════════════════════════════════════════════╗")
    println("║                      DATA NILAI MAHASISWA                         ║")
    println("╠════╦════════════╦════════════════════╦════════════════════╦═══════╣")
    println("║ No ║ NIM        ║ Nama               ║ Mata Kuliah        ║ Nilai ║")
    println("╠════╬════════════╬════════════════════╬════════════════════╬═══════╣")

    list.forEachIndexed { index, mhs ->
        println("║ %-2d ║ %-10s ║ %-18s ║ %-18s ║ %-5d ║".format(
            index + 1, mhs.nim, mhs.nama, mhs.mataKuliah, mhs.nilai
        ))
    }

    println("╚════╩════════════╩════════════════════╩════════════════════╩═══════╝")
}

fun status(list: List<NilaiMahasiswa>) {

    val lulus = list.filter { it.nilai >= 70 }
    val tidakLulus = list.filter { it.nilai < 70 }

    println("\n╔══════════════════════════╗")
    println("║     MAHASISWA LULUS      ║")
    println("╠══════════════════════════╣")

    lulus.forEachIndexed { i, mhs ->
        println("║ %-2d %-13s (%d | %s) ║".format(i + 1, mhs.nama, mhs.nilai, getGrade(mhs.nilai)))
    }

    println("╚══════════════════════════╝")
    println("Total lulus: ${lulus.size} mahasiswa\n")

    println("╔══════════════════════════════╗")
    println("║     MAHASISWA TIDAK LULUS    ║")
    println("╠══════════════════════════════╣")

    tidakLulus.forEachIndexed { i, mhs ->
        println("║ %-2d %-13s (%d | %s) ║".format(i + 1, mhs.nama, mhs.nilai, getGrade(mhs.nilai)))
    }

    println("╚══════════════════════════════╝")
    println("Total tidak lulus: ${tidakLulus.size} mahasiswa")
}

fun statistik(list: List<NilaiMahasiswa>) {

    val rataRata = list.map { it.nilai }.average()
    val tertinggi = list.maxByOrNull { it.nilai }
    val terendah = list.minByOrNull { it.nilai }

    println("\n╔════════════════════════════════════════════════════╗")
    println("║                   STATISTIK NILAI                  ║")
    println("╠════════════════════════════════════════════════════╣")
    println("║ Total Mahasiswa : %-32d ║".format(list.size))
    println("║ Rata-rata Nilai : %-32.1f ║".format(rataRata))
    println("║ Nilai Tertinggi : %-14d (%-15s) ║".format(tertinggi?.nilai, tertinggi?.nama))
    println("║ Nilai Terendah  : %-14d (%-15s) ║".format(terendah?.nilai, terendah?.nama))
    println("╚════════════════════════════════════════════════════╝")
}

fun shorting(list: List<NilaiMahasiswa>) {

    val ascending = list.sortedBy { it.nilai }
    val descending = list.sortedByDescending { it.nilai }

    println("\n╔════════════════════════════╗")
    println("║   NILAI ASCENDING          ║")
    println("╠════════════════════════════╣")

    ascending.forEachIndexed { i, mhs ->
        println("║ %-2d %-19s %3d ║".format(i + 1, mhs.nama, mhs.nilai))
    }

    println("╚════════════════════════════╝")

    println("\n╔════════════════════════════╗")
    println("║   NILAI DESCENDING         ║")
    println("╠════════════════════════════╣")

    descending.forEachIndexed { i, mhs ->
        println("║ %-2d %-19s %3d ║".format(i + 1, mhs.nama, mhs.nilai))
    }

    println("╚════════════════════════════╝")
}

fun gradeTampilan(list: List<NilaiMahasiswa>) {

    val perGrade = list.groupBy { getGrade(it.nilai) }

    println("\n╔════════════════════════════════╗")
    println("║        DATA BERDASARKAN GRADE  ║")
    println("╠════════════════════════════════╣")

    for (grade in listOf("A", "B", "C", "D", "E")) {

        val mahasiswaGrade = perGrade[grade] ?: emptyList()

        println("║ Grade $grade (${mahasiswaGrade.size} mahasiswa)")
        println("╟────────────────────────────────╢")

        if (mahasiswaGrade.isEmpty()) {
            println("║   Tidak ada")
        } else {
            mahasiswaGrade.forEach {
                println("║   - ${it.nama} (${it.nilai})")
            }
        }

        println("╟────────────────────────────────╢")
    }

    println("╚════════════════════════════════╝")
}

fun search(list: List<NilaiMahasiswa>, keyword: String) {

    print("\nMasukkan nama yang ingin dicari: ")
    val input = readLine() ?: ""

    val hasil = list.filter { it.nama.contains(input, ignoreCase = true) }

    println("\n╔══════════════════════════════════════════════════════════════╗")
    println("║                      HASIL PENCARIAN                         ║")
    println("╠════╦════════════════════╦════════════╦════════════╦══════════╣")
    println("║ No ║ Nama               ║ NIM        ║ Nilai      ║ Grade    ║")
    println("╠════╬════════════════════╬════════════╬════════════╬══════════╣")

    if (hasil.isEmpty()) {
        println("║                     Data tidak ditemukan                     ║")
    } else {
        hasil.forEachIndexed { i, mhs ->
            println("║ %-2d ║ %-18s ║ %-10s ║ %-10d ║ %-8s ║".format(
                i + 1,
                mhs.nama,
                mhs.nim,
                mhs.nilai,
                getGrade(mhs.nilai)
            ))
        }
    }

    println("╚════╩════════════════════╩════════════╩════════════╩══════════╝")
}