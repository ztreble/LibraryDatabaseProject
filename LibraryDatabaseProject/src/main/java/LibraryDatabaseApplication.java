
import domain.TableOperation;

import java.awt.*;
import java.io.File;
import java.util.Scanner;

public class LibraryDatabaseApplication {
    public static void main(String[] args) throws AWTException {
        boolean mark1=true;

        String catalog = "D:\\dbFiles";
        File file = new File(catalog);
        if(!file.exists()){
            file.mkdirs();
        }

        while(mark1){

            System.out.print("\t*********************************");
            System.out.print("\n\t*              MENU             *");
            System.out.print("\n\t*   1.CREATTABLE                *");
            System.out.print("\n\t*   2.INSERT                    *");
            System.out.print("\n\t*   3.REMOVE                    *");
            System.out.print("\n\t*   4.EXIT                      *");
            System.out.print("\n\t*********************************\n");

            System.out.print("\tPlease type your choice: ");
            Scanner scanner = new Scanner(System.in);
            String choice = scanner.nextLine();
            switch(choice){
                case "1":{
                    TableOperation.createTable();
                    break;
                }
                case "2":{
                    TableOperation.insert();
                    break;
                }
                case "3":{
                    TableOperation.remove();
                    break;
                }
                case "4":{
                    File dirFile = new File(catalog);
                    File[] files = dirFile.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        // 删除子文件
                        if (files[i].isFile())
                            files[i].delete();
                    }
                    return;
                }
                default:
                    System.out.print("\tYour type is wrong!Please type right option.\n");
                    break;
            }
        }

    }
}
