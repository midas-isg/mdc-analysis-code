package edu.pitt.isg;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;

public class SoftwareDatasetPair {


    public static HashMap<String, List<String>> csvToSoftwareDatasetPair(String csvFile) throws ParseException, IOException {
        List<String> csvRows = new ArrayList<String>();
        HashMap<String, List<String>> softwareDatasetPair = new HashMap<>();

        Reader reader = Files.newBufferedReader(Paths.get(csvFile));
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader("software_id+input_number","compatible_dataset_ids","software_name","input_name","compatible_dataset_names").withTrim().withIgnoreHeaderCase().withFirstRecordAsHeader());

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            for (CSVRecord csvRecord : csvParser) {
                String key = csvRecord.get("software_id+input_number");
                String value = csvRecord.get("compatible_dataset_ids");
                //remove { }
                value = value.replaceAll("[{}]", "");
                List<String> valueList = null;
                if (!(value.trim().length() == 0))
                    valueList = new LinkedList<>(Arrays.asList(value.split(",")));

                if(softwareDatasetPair.containsKey(key)){
                    System.out.println("error");
                }
                softwareDatasetPair.put(key, valueList);
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        return softwareDatasetPair;
    }

    public static void main(String[] args) throws IOException {
        String filename = "input-dataset-pairs.csv";
        try {
            HashMap<String, List<String>> softwareDatasetPair = csvToSoftwareDatasetPair(filename);

            //Example code
            Iterator<String> softwareIdAndInputNumber = softwareDatasetPair.keySet().iterator();
            while (softwareIdAndInputNumber.hasNext()) {
                String key = softwareIdAndInputNumber.next();
                System.out.print(key + " has compatible datasets: " );
                List<String> list = softwareDatasetPair.get(key);
                if (list == null) {
                    System.out.println("none");
                } else {
                    String validDatasets = "";
                    for (String validDataset : list) {
                        validDatasets += validDataset + ",";
                    }
                    validDatasets = validDatasets.substring(0, validDatasets.length() - 1);
                    System.out.println(validDatasets);
                }
            }
        }
        catch (ParseException e){
            e.printStackTrace();
        }
    }
}
