package ru.pocket.testcat.addressbook.generators;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
import ru.pocket.testcat.addressbook.model.ContactData;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Goblik on 03.10.2016.
 */
public class ContactGenerator {

    @Parameter(names = "-c", description = "Contact count")
    public int count;

    @Parameter(names = "-f", description = "Target file")
    public String file;

    @Parameter(names = "-d", description = "Data format")
    public String format;

    public static void main(String[] args) throws IOException {
      ContactGenerator generator = new ContactGenerator();
      JCommander jCommander = new JCommander(generator);
      try {
        jCommander.parse(args);
      }catch (ParameterException e) {
        jCommander.usage();
        return;
      }
      generator.run();
    }

    private void run() throws IOException {
      List<ContactData> contacts = generateContacts(count);
      if (format.equals("csv")) {
        saveAsDefoltCSV(contacts, new File(file));
      } else if (format.equals("xml")){
        saveAsXML(contacts, new File(file));
      } else if (format.equals("json")){
        saveAsJSON(contacts, new File(file));
      } else {
        System.out.println("Unrecognized format" + format);
      }
    }

    private void saveAsJSON(List<ContactData> contacts, File file) throws IOException {
      Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
      String json = gson.toJson(contacts);
      try(Writer writer = new FileWriter(file)){
        writer.write(json);

    }}

    private void saveAsXML(List<ContactData> contacts, File file) throws IOException {
      XStream xstream = new XStream();
      xstream.alias("contacts", ContactData.class);
      xstream.processAnnotations(ContactData.class);
      String xml = xstream.toXML(contacts);

      try (Writer writer = new FileWriter(file)) {
        writer.write(xml);

      }
    }
    private List<ContactData> generateContacts(int count) {
      List<ContactData> contacts = new ArrayList<ContactData>();
      for (int i = 0; i < count; i++) {
        contacts.add(new ContactData()
                .withFirstname(String.format("Firstname %s", i))
                .withLastname(String.format("Lastname %s", i))
                .withEmail(String.format("user%s@mail.ru", i))
                .withHomePhone(String.format("+7900123445%s",i)).withNewgroup("newgroup"));
      }
      return contacts;
    }

    private void saveAsDefoltCSV(List<ContactData> contacts, File file) throws IOException {
      Writer writer = new FileWriter(file);
      for (ContactData contact: contacts) {
        writer.write(String.format("%s;%s;%s;%s;\n",
                contact.getFirstname(),
                contact.getLastname(),
                contact.getEmail(),
                contact.getHomePhone(),
                contact.getNewgroup()));
      }
    }
  }
