data class Produk(
    val id: String,
    val nama: String,
    val harga: Double,
    val kategori: String,
    var stok: Int
)

data class CartItem(
    val produk: Produk,
    var jumlah: Int
)

data class Customer(
    val id: String,
    val nama: String,
    val email: String,
    val alamat: String? = null 
)

data class Order(
    val id: String,
    val customer: Customer,
    val items: List<CartItem>,
    var status: OrderStatus,
    val paymentMethod: PaymentMethod,
    val totalHarga: Double,
    val diskon: Double = 0.0
)

sealed class OrderStatus {
    object Pending    : OrderStatus()
    object Processing : OrderStatus()
    object Shipped    : OrderStatus()
    object Delivered  : OrderStatus()
    object Cancelled  : OrderStatus()

    fun label(): String = when (this) {
        is Pending    -> "Menunggu Konfirmasi"
        is Processing -> "Sedang Diproses"
        is Shipped    -> "Dalam Pengiriman"
        is Delivered  -> "Telah Diterima"
        is Cancelled  -> "Dibatalkan"
    }
}

sealed class PaymentMethod {
    object Cash                          : PaymentMethod()
    data class Transfer(val bank: String): PaymentMethod()
    data class EWallet(val platform: String): PaymentMethod()

    fun label(): String = when (this) {
        is Cash              -> "Tunai"
        is Transfer          -> "Transfer Bank ($bank)"
        is EWallet           -> "E-Wallet ($platform)"
    }
}

interface Discount {
    fun hitungDiskon(hargaAsli: Double): Double
    fun labelDiskon(): String
}

class DiskonPersen(private val persen: Int) : Discount {
    override fun hitungDiskon(hargaAsli: Double): Double {
        return hargaAsli * (persen / 100.0)
    }
    override fun labelDiskon(): String = "Diskon $persen%"
}

class DiskonNominal(private val nominal: Double) : Discount {
    override fun hitungDiskon(hargaAsli: Double): Double {
        return if (nominal > hargaAsli) hargaAsli else nominal
    }
    override fun labelDiskon(): String = "Potongan Rp${formatRupiah(nominal)}"
}

class ShoppingCart {
    private val items = mutableListOf<CartItem>()

    fun tambahProduk(produk: Produk, jumlah: Int = 1): Boolean {
        if (produk.stok < jumlah) {
            println("  Stok tidak cukup! Stok tersedia: ${produk.stok}")
            return false
        }

        val existing = items.find { it.produk.id == produk.id }
        if (existing != null) {
            val totalJumlah = existing.jumlah + jumlah
            if (totalJumlah > produk.stok) {
                println("  Stok ndk cukup bro!")
                return false
            }
            existing.jumlah = totalJumlah
        } else {
            items.add(CartItem(produk, jumlah))
        }
        println("  ${produk.nama} (x$jumlah) ditambahkan ke keranjang.")
        return true
    }

    fun hapusProduk(produkId: String): Boolean {
        val item = items.find { it.produk.id == produkId }
        return if (item != null) {
            items.remove(item)
            println("  ${item.produk.nama} dihapus dari keranjang.")
            true
        } else {
            println("  produknya belum masuk keranjang bro!")
            false
        }
    }

    fun hitungSubtotal(): Double {
        return items.sumOf { it.produk.harga * it.jumlah }
    }

    fun hitungTotal(discountCalculator: ((Double) -> Double)? = null): Double {
        val subtotal = hitungSubtotal()
        val diskon = discountCalculator?.invoke(subtotal) ?: 0.0
        return subtotal - diskon
    }

    fun getItems(): List<CartItem> = items.toList()

    fun isEmpty(): Boolean = items.isEmpty()

    fun kosongkan() = items.clear()

    fun tampilkan() {
        if (items.isEmpty()) {
            println("╔════════════════════════════════╗")
            println("║      KERANJANG BELANJA KOSONG  ║")
            println("╚════════════════════════════════╝")
            return
        }

        println("\n╔════════════════════════════════════════════════════════════╗")
        println("║                     KERANJANG BELANJA                      ║")
        println("╠══════════════════════╦══════╦══════════════╦═══════════════╣")
        println("║ Produk               ║ Qty  ║ Harga        ║ Subtotal      ║")
        println("╠══════════════════════╬══════╬══════════════╬═══════════════╣")

        items.forEach { item ->
            val subtotal = item.produk.harga * item.jumlah

            println(
                "║ %-20s ║ %-4d ║ %-12s ║ %-13s ║".format(
                    item.produk.nama,
                    item.jumlah,
                    "Rp${formatRupiah(item.produk.harga)}",
                    "Rp${formatRupiah(subtotal)}"
                )
            )
        }

        println("╠══════════════════════╩══════╩══════════════╩═══════════════╣")
        println("║ Subtotal : Rp${formatRupiah(hitungSubtotal())}                              ║")
        println("╚════════════════════════════════════════════════════════════╝")
    }
}

class TokoOnline(val namaToko: String) {
    private val produkList  = mutableListOf<Produk>()
    private val orderList   = mutableListOf<Order>()
    private var orderCounter = 1

    fun tambahProduk(produk: Produk) {
        produkList.add(produk)
    }

    fun tampilkanProduk() {
        println("\n╔══════════════════════════════════════════════════════════════╗")
        println("║                    DAFTAR PRODUK $namaToko                   ║")
        println("╠════════╦══════════════════════════╦══════════════╦═══════════╦══════╗")
        println("║ ID     ║ Nama Produk              ║ Harga        ║ Kategori  ║ Stok ║")
        println("╠════════╬══════════════════════════╬══════════════╬═══════════╬══════╣")

        if (produkList.isEmpty()) {
            println("║                 BELUM ADA PRODUK TERSEDIA                   ║")
        }

        produkList.forEach { p ->
            val stokLabel = if (p.stok == 0) "Habis" else p.stok.toString()

            println(
                "║ %-6s ║ %-24s ║ %-12s ║ %-9s ║ %-4s ║".format(
                    p.id,
                    p.nama,
                    "Rp${formatRupiah(p.harga)}",
                    p.kategori,
                    stokLabel
                )
            )
        }

        println("╚════════╩══════════════════════════╩══════════════╩═══════════╩══════╝")
    }

    fun filterProdukByKategori(kategori: String): List<Produk> {
        return produkList.filter { it.kategori.equals(kategori, ignoreCase = true) }
    }

    fun produkTermurah(): List<Produk> = produkList.sortedBy { it.harga }
    fun produkTermahal(): List<Produk> = produkList.sortedByDescending { it.harga }

    fun cariProduk(keyword: String): List<Produk> {
        return produkList.filter { it.nama.contains(keyword, ignoreCase = true) }
    }

    fun checkout(
        customer: Customer,
        cart: ShoppingCart,
        paymentMethod: PaymentMethod,
        diskon: Discount? = null
    ): Order? {
        if (cart.isEmpty()) {
            println("  Keranjang belanja kosong! Tidak bisa checkout.")
            return null
        }

        val subtotal = cart.hitungSubtotal()

        val totalDiskon = diskon?.hitungDiskon(subtotal) ?: 0.0
        val total = cart.hitungTotal { diskon?.hitungDiskon(it) ?: 0.0 }

        cart.getItems().forEach { item ->
            val produk = produkList.find { it.id == item.produk.id }
            produk?.let {
            it.stok -= item.jumlah
        }
        }

        val orderId = "ORD-%03d".format(orderCounter++)
        val order = Order(
            id            = orderId,
            customer      = customer,
            items         = cart.getItems(),
            status        = OrderStatus.Pending,
            paymentMethod = paymentMethod,
            totalHarga    = total,
            diskon        = totalDiskon
        )

        orderList.add(order)
        cart.kosongkan()

        return order
    }

    fun updateStatus(orderId: String, statusBaru: OrderStatus): Boolean {
        val order = orderList.find { it.id == orderId }
        return if (order != null) {
            order.status = statusBaru
            println("  Status pesanan $orderId diperbarui: ${statusBaru.label()}")
            true
        } else {
            println("  Pesanan $orderId tidak ditemukan.")
            false
        }
    }

    fun tampilkanOrder(order: Order) {

    println("\n╔════════════════════════════════════════════════════════════╗")
    println("║                     DETAIL PESANAN ${order.id}              ║")
    println("╠════════════════════════════════════════════════════════════╣")

    println("║ Customer   : %-44s ║".format(order.customer.nama))
    println("║ Email      : %-44s ║".format(order.customer.email))
    println("║ Alamat     : %-44s ║".format(order.customer.alamat ?: "Belum diisi"))
    println("║ Pembayaran : %-44s ║".format(order.paymentMethod.label()))
    println("║ Status     : %-44s ║".format(order.status.label()))

    println("╠══════════════════════╦══════╦══════════════╦═══════════════╣")
    println("║ Produk               ║ Qty  ║ Harga        ║ Subtotal      ║")
    println("╠══════════════════════╬══════╬══════════════╬═══════════════╣")

    order.items.forEach { item ->

        val subtotal = item.produk.harga * item.jumlah

        println(
            "║ %-20s ║ %-4d ║ %-12s ║ %-13s ║".format(
                item.produk.nama,
                item.jumlah,
                "Rp${formatRupiah(item.produk.harga)}",
                "Rp${formatRupiah(subtotal)}"
            )
        )
    }

    val subtotal = order.items.sumOf { it.produk.harga * it.jumlah }

    println("╠══════════════════════╩══════╩══════════════╩═══════════════╣")
    println("║ Subtotal : Rp${formatRupiah(subtotal)}                               ║")

    if (order.diskon > 0) {
        println("║ Diskon   : -Rp${formatRupiah(order.diskon)}                           ║")
    }

    println("║ TOTAL    : Rp${formatRupiah(order.totalHarga)}                        ║")
    println("╚════════════════════════════════════════════════════════════╝")
    }

    // Tampilkan semua riwayat pesanan
    fun tampilkanRiwayatPesanan() {
        println("\n===== RIWAYAT PESANAN =====")
        if (orderList.isEmpty()) {
            println("Belum ada pesanan.")
            return
        }
        orderList.forEach { order ->
            println("${order.id} | ${order.customer.nama} | Rp${formatRupiah(order.totalHarga)} | ${order.status.label()}")
        }
    }

    fun statistikToko() {

        val totalPendapatan = orderList
            .filter { it.status is OrderStatus.Delivered }
            .sumOf { it.totalHarga }

        val totalPesanan = orderList.size
        val pesananSelesai = orderList.count { it.status is OrderStatus.Delivered }

        println("\n╔════════════════════════════════════╗")
        println("║           STATISTIK TOKO           ║")
        println("╠════════════════════════════════════╣")
        println("║ Total Pesanan   : %-15d ║".format(totalPesanan))
        println("║ Pesanan Selesai : %-15d ║".format(pesananSelesai))
        println("║ Total Pendapatan: Rp%-13s ║".format(formatRupiah(totalPendapatan)))
        println("╚════════════════════════════════════╝")
    }
}

fun formatRupiah(angka: Double): String {
    val rounded = angka.toInt()
    return "%,d".format(rounded).replace(',', '.')
}

fun main() {
    val toko = TokoOnline("Toko Konoha")

    toko.tambahProduk(Produk("JKW1", "Sawit", 9999999.0, "Rahasia", 5))
    toko.tambahProduk(Produk("JKW2", "Jabatan", 99999999999.0, "Rahasia", 20))
    toko.tambahProduk(Produk("JKW3", "Etanol", 9999999.0, "Rahasia", 10))
    toko.tambahProduk(Produk("JKW4", "Baju", 85000.0, "Fashion", 50))
    toko.tambahProduk(Produk("JKW5", "Celana", 200000.0, "Fashion", 30))
    toko.tambahProduk(Produk("JKW6", "Sepatu", 350000.0, "Fashion", 15))
    toko.tambahProduk(Produk("JKW7", "Buku jawa", 95000.0, "Buku", 25))
    toko.tambahProduk(Produk("JKW8", "Antek-antek Asing", 7050000.0, "Rahasia", 8))

    toko.tampilkanProduk()

    println("\n===== PRODUK KATEGORI: Rahasia =====")
    toko.filterProdukByKategori("Rahasia").forEach {
        println("  - ${it.nama} | Rp${formatRupiah(it.harga)}")
    }

    println("\n===== PRODUK TERMURAH KE TERMAHAL =====")
    toko.produkTermurah().forEach {
        println("  - ${it.nama} | Rp${formatRupiah(it.harga)}")
    }

    println("\n===== CARI PRODUK: \"mouse\" =====")
    val hasil = toko.cariProduk("mouse")
    if (hasil.isEmpty()) println("  Tidak ditemukan.")
    else hasil.forEach { println("  - ${it.nama}") }

    val customer1 = Customer("PRB1", "Jokowi", "owi@email.com", "Tembok Ratapan Solo")
    val customer2 = Customer("PRB2", "Gibrun",   "FUFUFAFA@email.com")

    println("\n========================================")
    println("  TRANSAKSI 1 - ${customer1.nama}")
    println("========================================")

    val cart1 = ShoppingCart()
    println("\n[Tambah ke Keranjang]")
    cart1.tambahProduk(Produk("JKW1", "Sawit", 9999999.0, "Rahasia", 5), 1)
    cart1.tambahProduk(Produk("JKW2", "Jabatan", 99999999.0, "Rahasia", 20), 2)
    cart1.tambahProduk(Produk("JKW3", "Etanol", 9999999.0, "Rahasia", 10), 1)

    cart1.tampilkan()

    println("\n[Hapus Produk dari Keranjang]")
    cart1.hapusProduk("JKW3")
    cart1.tampilkan()

    val diskon1 = DiskonPersen(10)
    val subtotal1 = cart1.hitungSubtotal()
    println("\n[Diskon] ${diskon1.labelDiskon()} = -Rp${formatRupiah(diskon1.hitungDiskon(subtotal1))}")

    val order1 = toko.checkout(
        customer      = customer1,
        cart          = cart1,
        paymentMethod = PaymentMethod.Transfer("BCA"),
        diskon        = diskon1
    )

    order1?.let {
        toko.tampilkanOrder(it)
        println("\n[Update Status Pesanan]")
        toko.updateStatus(it.id, OrderStatus.Processing)
        toko.updateStatus(it.id, OrderStatus.Shipped)
        toko.updateStatus(it.id, OrderStatus.Delivered)
    }

    println("\n========================================")
    println("  TRANSAKSI 2 - ${customer2.nama}")
    println("========================================")

    val cart2 = ShoppingCart()
    println("\n[Tambah ke Keranjang]")
    cart2.tambahProduk(Produk("JKW4", "Baju",        85000.0, "Fashion", 50), 3)
    cart2.tambahProduk(Produk("JKW7", "Buku jawa", 95000.0, "Buku",   25), 2)

    cart2.tampilkan()

    val diskon2 = DiskonNominal(50000.0)
    val subtotal2 = cart2.hitungSubtotal()
    println("\n[Diskon] ${diskon2.labelDiskon()} = -Rp${formatRupiah(diskon2.hitungDiskon(subtotal2))}")

    val order2 = toko.checkout(
        customer      = customer2,
        cart          = cart2,
        paymentMethod = PaymentMethod.EWallet("Dana"),
        diskon        = diskon2
    )

    order2?.let {
        toko.tampilkanOrder(it)
        println("\n[Update Status Pesanan]")
        toko.updateStatus(it.id, OrderStatus.Processing)
        toko.updateStatus(it.id, OrderStatus.Cancelled)
    }

    toko.tampilkanRiwayatPesanan()
    toko.statistikToko()

    println("\n===== Makasih Bro dah Belanja di ${toko.namaToko}! =====")
}