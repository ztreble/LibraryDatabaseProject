package domain;
import com.linuxense.javadbf.*;

import java.io.*;
import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;
import com.linuxense.javadbf.DBFWriter;


public class TableOperation {
    public static void createTable(){
        String tablename;
        LinkedHashMap<String,Integer> map = new LinkedHashMap<String,Integer>();
        Scanner scanner = new Scanner(System.in);
        System.out.print("\tenter table name:");
        tablename = scanner.nextLine();
        DBFWriter writer=null;
        while(true){
            String name;
            int length;
            System.out.print("\tenter column name and its length(quit by type -1):");
            name = scanner.next();
            if(name.equals("-1")) break;
            length = scanner.nextInt();
            map.put(name,length);
        }

        if(!tablename.equals("")){
            String path = "D:\\dbFiles\\"+tablename+".dbf";
            File file = new File(path);

            try{
                if(!file.exists())
                    file.createNewFile();
                DBFField[] fields = new DBFField[map.size()];
                Iterator iterator = map.entrySet().iterator();
                int i=0;
                while(iterator.hasNext()){
                    Map.Entry<String,Integer> entry = (Map.Entry<String,Integer>)iterator.next();
                    fields[i] = new DBFField();
                    fields[i].setName(entry.getKey());
                    int tmp = entry.getValue();
                    fields[i].setType(DBFDataType.CHARACTER);
                    fields[i].setLength(tmp);
                    i++;
                }
                writer = new DBFWriter(file);
                writer.setFields(fields);

                System.out.println("The dbf file product success!");
            }catch (IOException e) {
                System.out.println("The dbf file product failed!");
                e.printStackTrace();
            }finally {
                DBFUtils.close(writer);
            }
        }
    }
    public static void insert(){
        File f;
        Scanner scanner = new Scanner(System.in);
        String filename;
        OutputStream fos=null;
        InputStream fis =null;
        DBFReader reader  =null;
        DBFWriter writer =null;

        while(true){
            System.out.print("\tenter file name:");
            filename = scanner.next();
            filename = "d:\\dbFiles\\"+filename+".dbf";
            f = new File(filename);
            if(f.exists()) break;
            else System.out.println("\tPlease type right filename!");
        }


        try {
            boolean mark = true;
            List<String> fieldsList = new ArrayList<>();
            fis = new FileInputStream(filename);
            reader  = new DBFReader(fis);
            int fieldsCount = reader.getFieldCount();
            for(int i=0;i<fieldsCount;i++){
                fieldsList.add(reader.getField(i).getName());
            }

            DBFUtils.close(reader);
            fis.close();

            writer = new DBFWriter(new File(filename));

            do{
                Object[] rowData = new Object[fieldsCount];
                String tmpString = null;
                for(int i=0;i<fieldsCount;i++){
                    System.out.printf("\t"+fieldsList.get(i)+":");
                    tmpString = scanner.next();
                    rowData[i] = tmpString;
                }
                writer.addRecord(rowData);
                while(true){
                    System.out.printf("\tMore entries (Y/n):");
                    tmpString = scanner.next();
                    if(tmpString.equals("Y")||tmpString.equals("y")){
                        break;
                    }
                    else if(tmpString.equals("n")||tmpString.equals("N")){
                        mark = false;
                        break;
                    }
                    else{
                        System.out.println("\tYour Type wrong!");
                    }
                }

            }while(mark);
            writer.write();
            DBFUtils.close(writer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }


    public static void createTable(String tablename,DBFField[] fields,List<String> fieldsList,List<String> outList,int del){

        File file = new File(tablename);
        DBFWriter writer=null;
        writer = new DBFWriter(new File(tablename));
        writer.setFields(fields);
        DBFUtils.close(writer);
        writer = new DBFWriter(new File(tablename));
        Object[] rowData = new Object[fieldsList.size()];
        for(int i=0;i<outList.size()/fieldsList.size();i++){
            if(i==del) continue;
            for(int j=0;j<fieldsList.size();j++){
                rowData[j]=outList.get(i*fieldsList.size()+j);
            }
            writer.addRecord(rowData);
        }
        writer.write();
}
    public static void remove() {
        File f;
        Scanner scanner = new Scanner(System.in);
        String filename;
        OutputStream fos=null;
        InputStream fis =null;
        DBFReader reader  =null;
        DBFWriter writer =null;

        while(true){
            System.out.print("\tenter file name:");
            filename = scanner.next();
            filename = "d:\\dbFiles\\"+filename+".dbf";
            f = new File(filename);
            if(f.exists()) break;
            else System.out.println("\tPlease type right filename!");
        }

        while(true){
            print(filename);
            System.out.print("Please enter the number of lines to delete(quit by type -1):");
            int num = scanner.nextInt();

            if(num==-1) break;

            else try {
                List<String> fieldsList = new ArrayList<>();
                List<String> outList = new ArrayList<>();
                fis = new FileInputStream(filename);

                reader  = new DBFReader(fis);
                int fieldsCount = reader.getFieldCount();
                DBFField[] fields = new DBFField[fieldsCount];
                for(int i=0;i<fieldsCount;i++){
                    fields[i]=reader.getField(i);
                    fieldsList.add(reader.getField(i).getName());
                }
                Object[] rowValues;
                while ((rowValues = reader.nextRecord()) != null) {
                    for (int i = 0; i < rowValues.length; i++) {
                        outList.add((String) rowValues[i]);
                    }
                }
                DBFUtils.close(reader);
                fis.close();
                f.delete();

                createTable(filename,fields,fieldsList,outList,num-1);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
        }



    }
    public static void print(String filename) {

        InputStream fis = null;
        try {
            fis = new FileInputStream(filename);
            DBFReader reader = new DBFReader(fis);
            int fieldsCount = reader.getFieldCount();
            System.out.printf("#\t");
            for (int i = 0; i < fieldsCount; i++) {
                DBFField field = reader.getField(i);
                System.out.print(" "+field.getName());
            }
            System.out.println("");
            Object[] rowValues;
            int  j=1;
            while ((rowValues = reader.nextRecord()) != null) {
                System.out.printf("#"+j+"\t");
                j++;
                for (int i = 0; i < rowValues.length; i++) {
                    System.out.print(" "+rowValues[i]);
                }
                System.out.println("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }

    }



}
