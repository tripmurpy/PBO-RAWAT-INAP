oke kamu akan menyempurnakan project ini .
1.
kita memperbaiki WARNA UI YG CENDERUNG GELAP 
KITA BUTUH WARNA YG CERAH , CALMING
DO NOT using DARK theme .

@image1 - @image2
ubah theme manjadi cerah , calming , easy to use
 1.Loginform.jepg 
  ubah ui ini menjadi cerah
  2.MainDashboard.jepg
  ubah ui ini menjadi cerah dan calming dan clean
  Pastikan dashboard easy to use
  3.pendaftaran pasien.jepg
  -form tanggal lahir harus di lengkapi dengan kalender agar mudah di input  .
  -pilih gender harus menggunakan radio button
  -asuransi juga radio button (ubah hanya bpjs , asuransi kesehatan eka hospital dan asuransi swasta)
   4.Daftar pasien.jepg (jika add pasien di pendaftaran maka data akan di pindah layer ini)
   -dalam layer ini akan ada informasi untuk siapa dokter yg akan melayani pasien (radio button)
   -perbaiki layout agar rapih
   -gunakann layout seperti antrian , agar orang bisa lebih jelas melihat nomor antriannya
    dan info ruangannya . dan nomor telepon yang terdaftar.
    5.Rawat inap 
     ini adalah layer yg memberikan data about pasien yg sedang di rawat
     -ubah layout menjadi lebih baik
     -tambahkan kolom informasi 
     data yg di perlukan adalah nama pasien , no medical record, no antrian, no telepon , jenis kelamin , 
     tanggal masuk ruang inap , jenis kamar , no kamar , dokter jaga
     6.Pasien keluar (Berfungsi untuk mengambil data pasien yg sudah keluar dan sudah membayar)
      pakai radio button untuk menyelesaikan , dan pakai layout table.
    7.Management dokter  (fungsinya adalah daftar dari dokter di rumah sakit ini )
    -nama dokter
    -harga konsultasi
    -jam kerja
    -keahlian(checkbox:Dokter umum, Dokter spesialis, Dokter bedah)
    (bentuk Ui seperti etalase dengan foto dokter (just fake foto profile))

    8.Management kamar 
    -gunakan format table untuk menampilkan data
    -data yg di tampilkan nama kamar, jenis kamar, tarif per hari, 
    -AKU INGIN TAMPILAN KAMAR ITU SEPERTI MAU PESEN BIOSKOP
    HIJAU UNTUK READY MERAH UNTUK TERISI

    9.Management obat 
     berfungsi untuk memindai obat untuk pasien
     dalam layer ini ada list pasien yg  bisa di click untuk menunjukan obat apa saja yg dibutuhkan
     ada jam minum obat









     

     8.Billing  (Semua biaya akan di total di sini)
        di layer ini berisi banyak layer yang harus di tampilkan
         ini adalah contoh layer yg harus muncul  
         A.Informasi pasien (nama pasien, no medical record, tanggal masuk, tanggal keluar, harga konsultasi)
         B.pilihan tindakan(pemeriksaan, tindakan medis, pembelian obat dan alat kesehatan)
         D.pemilihan obat & alat kesehatan ini wajib menggunakan checkbox dan combobox untuk memilih jumlah)
         E.pilihan tindakan medis ini ada beberapa pilihan (infus, rontgen, operasi,Rawat inap)
          pemilihan ini harus menggunakan radio button
         F.untuk tindakan operasi, rontgen, rawat inap dan infus ada sub layer lagi untuk mengisi detail tindakan 
           -di dalam nya terdapat harga tindakan , biaya admin, dan harga kamarnya (jika rawat inap)
           -jika operasi ada input untuk dokter bedah dan dokter asisten
           -biaya operasi  dan tindakan lainnya menggunakan input number.
          -
        G.pilihan dokter jaga (menggunakan radio button)
        H.harga dokter jaga menggunakan input number dan sudah terisi otomatis mengikuti dokter jaga (sudah ada di layer management dokter)
        I.pilihan ruangan (menggunakan radio button, dan tersedia pilihan:)
          -kelas VIP(memakai tag silver)
          -kelas 1(memakai tag gold)
          -kelas 2(memakai tag cyan)
          -kelas 3(memakai tag blue)
          -kelas perawatan khusus(memakai tag red)
        J.harga kamar menggunakan input number dan sudah terisi otomatis mengikuti harga ruangan (sudah ada di layer Management Kamar)
        K.pilihan obat & alat kesehatan (menggunakan checkbox dan combobox untuk memilih jumlah)
        L.pembayaran:
          -tunai
          -credit/debit
          -transfer
          -bpjs(menggunakan radio button)
           info pasien bpjs akan di ambil dari pasien bpjs
        M.total tagihan (total ini akan menjumlahkan semua biaya tindakan medis, biaya dokter, biaya ruangan, obat dan alat kesehatan dan biaya tindakan medis lainnya)
        N.tombol simpan(jika sudah selesai semua ,tombol ini digunakan untuk menyimpan semua tagihan)
        O.tombol kembali(jika sudah selesai semua ,tombol ini digunakan untuk kembali)
        P.buat laporan tagihan (jika sudah selesai semua, tombol ini digunakan untuk membuat laporan tagihan, simpan sebagai pdf)
        
    9.Laporan pasin keluar 
     ini adalah tampilan laporan pasien keluar (dalam laporan ini sudah ada  semua total biaya , total obat , total tindakan , total tindakan lainnya)
     -ubah layout menjadi lebih baik
     -tambahkan kolom informasi 
     data yg di perlukan adalah nama pasien , no medical record, no antrian, no telepon , jenis kelamin , 
     tanggal masuk ruang inap , jenis kamar , no kamar , dokter jaga , harga dokter jaga , harga ruangan , harga obat dan alat kesehatan , harga tindakan , harga tindakan lainnya , 
     total biaya 
     -dalam laporan ini ada info mengenai tagihan bpjs, BPJS juga akan di hitung di total tagihan
     -dalam laporan ini juga ada tombol untuk mengunduh laporan dalam bentuk pdf.
        10.Statistik 
          menampilkan grafik mengenai data pasien, dokter, ruangan, obat dan alat kesehatan , tindakan, tindakan lainnya , total biaya
          gunakanlah Grafik Pie, dan grafik Batang dan grafik garis.
           
    
  

    
  