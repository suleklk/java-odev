package odev4;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class odev4 {
    
    public static void main(String[] args) throws IOException {   
        
        int faktoriyelsayi,bas=1,sayac=0;   
        double sure,nanosure;  //sure icin tanımlamalar yapıldı
        Sayi deger= new Sayi("1"); 
       
        //dısarıdan sayı alındı
        Scanner oku=new Scanner(System.in); 
        System.out.print("Bir sayı giriniz: ");   
        faktoriyelsayi=oku.nextInt();
        long basla = System.nanoTime();
        
        //seri faktoriyel hesabı yapıldı.
        deger.SeriFaktoriyel(faktoriyelsayi);
        nanosure=System.nanoTime();
        sure = (nanosure - basla)/1000000.0;
        
        //sonuc milisaniye seklinde ekrana yazıldı
        System.out.println("Seri Hesaplanma Süresi    :  "+String.format("%.2f", sure) + " ms");    
        
        basla = System.nanoTime();
        
        int thread;
       
        //thread sayısı dinamik olarak ayarlanması için işlemler yapıldı
        if(faktoriyelsayi==0){
         thread=1;   
        }
        else if(faktoriyelsayi<=50){
            thread=faktoriyelsayi/2;
        }
       
        else{
            thread=faktoriyelsayi/40;
        }
      
        
        Sayi[] sayilar = new Sayi[thread]; 
        
        //paralel hesaplama için havuz olusturuldu
        ExecutorService havuz = Executors.newFixedThreadPool(thread);
        
        //paralel hesaplama için sayıyı böldük
        int sayac2=0;
     do{
         sayilar[sayac2] = new Sayi("1");
          int son,y;
        //sayı 0 olursa bu işlem yapılır
          if(faktoriyelsayi==0){
              havuz.execute(new Faktoriyel(bas,faktoriyelsayi,sayilar[sayac2]));   
            }   
        //sayı 0 harici diger sayılar icin işlem yapıldı  
         else{  
            son= faktoriyelsayi/thread+bas;
          
            if(son>faktoriyelsayi) son=faktoriyelsayi;
            havuz.execute(new Faktoriyel(bas,son,sayilar[sayac2]));
           
            y=son++;
            bas=y;
            
            sayac2=sayac2+1;
            }
          
        }while(sayac2<thread);
           
        havuz.shutdown();
    //havuz kontrol edildi
        while(!havuz.isTerminated());
        //parcalanan sayılar biribirleriyle carpıldı
        while(sayac<thread)
        {
            deger.deger(sayilar[sayac].sayi.multiply(deger.sayi));
            sayac++;
        }
      //dosyaya yazdırma işlemi icin bigInteger tanımlanan sayı stringe cevrildi
        String sonuc= String.valueOf(deger.sayi);
        //dosyaya yazdırma işlemi yapıldı
        deger.yaz(sonuc);
        
        //paralel hesaplama suresi hesaplanıp yazdırıldı
        sure = (System.nanoTime() - basla)/1000000.0;
        System.out.println("Paralel Hesaplanma Süresi  : " +String.format("%.2f", sure) + " ms");    
        System.out.println("Sonuç Dosyaya Yazıldı...");
    }
    
}
