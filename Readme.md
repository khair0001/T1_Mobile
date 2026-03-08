## Identitas
**Nama**: [Ahmad Muslihul Khair]  
**NIM**: [F1D02310001]  
**Mata Kuliah**: Pemrograman Bergerak 
---

## 1. Sistem Toko Online (TokoOnline.kt)

### Deskripsi
Aplikasi simulasi toko online seperti yang disuruh

### Fitur
- Manajemen produk dengan kategori
- Keranjang belanja dengan validasi stok otomatis
- Multiple metode pembayaran (Cash, Transfer, E-Wallet)
- Sistem diskon (persentase dan nominal)
- Tracking status pesanan
- Filter dan pencarian produk
- Statistik toko

### Struktur Data

**Struktur pentingnya**
- `Produk` - nyimpen info produk (id, nama, harga, kategori, stok)
- `CartItem` - buat nampung item di keranjang sama jumlahnya
- `Customer` - data pelanggan, alamat optional
- `Order` - data pesanan lengkap dengan semua detailnya
- `OrderStatus` - status pesanan (Pending, Processing, Shipped, Delivered, Cancelled)
- `PaymentMethod` - metode pembayaran (Cash, Transfer, E-Wallet)
- `Discount` - interface buat sistem diskon, ada 2 implementasi: DiskonPersen dan DiskonNominal

### Penjelasan Fungsi

**Class ShoppingCart:**
```kotlin
tambahProduk() // nambahin produk ke keranjang, ada validasi stok
hapusProduk() // hapus produk dari keranjang berdasarkan ID
hitungSubtotal() // hitung total harga sebelum diskon
hitungTotal() // hitung total akhir pake lambda function buat diskon
tampilkan() // nampilin isi keranjang dalam bentuk tabel
```

**Class TokoOnline:**
```kotlin
tambahProduk() // nambahin produk baru ke toko
tampilkanProduk() // nampilin semua produk dalam tabel
filterProdukByKategori() // filter produk berdasarkan kategori pake filter function
produkTermurah() & produkTermahal() // ngurutin produk berdasarkan harga
cariProduk() // cari produk berdasarkan keyword nama
checkout() // proses transaksi, kurangin stok, bikin order baru
updateStatus() // update status pesanan
tampilkanOrder() // nampilin detail pesanan dalam tabel
statistikToko() // nampilin statistik total pesanan dan pendapatan
```

**tambahan dikit:**
```kotlin
formatRupiah() // biar ada .
```

### Screenshot
![Daftar Produk]()
![Keranjang Belanja]()
![Detail Pesanan]()
![Statistik Toko]()

---

## 2. Sistem Penilaian Mahasiswa (Tugas3_Penilaian.kt)

### Deskripsi
Aplikasi buat hitung nilai akhir mahasiswa berdasarkan UTS (30%), UAS (40%), dan Tugas (30%). Simple tapi lengkap dengan validasi input dan konversi grade.

### Fitur
- Input nilai dengan validasi (0-100)
- Perhitungan nilai akhir otomatis
- Konversi nilai ke grade (A, B, C, D, E)
- Penentuan status kelulusan (minimal 60)
- Saran berdasarkan grade

### Penjelasan Fungsi

```kotlin
main() 
// ambil input nama dan nilai, hitung nilai akhir, tampilin hasil lengkap

inputNilai() 
// validasi input nilai biar tetep di range 0-100 pake loop

grade() 
// konversi nilai jadi grade huruf (A-E) pake when expression

keteranganNilai() 
// kasih keterangan buat setiap grade (Sangat Baik, Baik, Cukup, dll)

getSaran() 
// kasih saran atau feedback berdasarkan grade yang didapet
```

### Screenshot
![Input Nilai]()
![Hasil Penilaian]()

---

## 3. Manajemen Data Nilai dengan Collections (Tugas7_Collections.kt)

### Deskripsi
Aplikasi buat manage dan analisis data nilai mahasiswa pake Kotlin Collections API. Belajar filter, map, groupBy, dan operasi collection lainnya.

### Fitur
- Tampilan semua data mahasiswa
- Filter mahasiswa lulus/tidak lulus (batas 70)
- Statistik nilai (rata-rata, tertinggi, terendah)
- Sorting ascending dan descending
- Grouping berdasarkan grade
- Pencarian mahasiswa by nama

### Struktur Data

**Data Class:**
- `NilaiMahasiswa` - nyimpen data mahasiswa (nim, nama, mataKuliah, nilai)

### Penjelasan Fungsi

```kotlin
getGrade() 
// konversi nilai jadi grade huruf pake when expression

tampilkanSemuaData() 
// nampilin semua data mahasiswa dalam tabel pake forEachIndexed

status() 
// filter dan tampilin mahasiswa yang lulus (≥70) dan tidak lulus (<70) pake filter function

statistik() 
// hitung rata-rata pake map dan average, cari nilai tertinggi dan terendah pake maxByOrNull dan minByOrNull

shorting() 
// ngurutin data mahasiswa berdasarkan nilai ascending dan descending pake sortedBy dan sortedByDescending

gradeTampilan() 
// kelompokkan mahasiswa berdasarkan grade pake groupBy function

search() 
// cari mahasiswa berdasarkan nama pake filter dan contains (case-insensitive)
```

### Screenshot
![Data Mahasiswa]()
![Status Kelulusan]()
![Statistik]()
