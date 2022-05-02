package com.company.Commands;

import com.company.Enums.FormOfEducation;
import com.company.Main;
import com.company.Utility.AnnotationNew;
import com.company.Utility.Message;
import com.company.classes.Coordinates;
import com.company.classes.Person;
import com.company.classes.StudyGroup;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;
import static com.company.Main.message;
import static com.company.Main.object;
import static com.company.Utility.Server.*;

public class AllCommands {

    public static HashMap<String, Method> map = new HashMap<>();//Создадим объект класса HashMap:

    public static AllCommands com = new AllCommands();//Объект класса с командами

    static {

        for (Method met : com.getClass().getDeclaredMethods()) {//Берем список всех методов в классе Commands

            if (met.isAnnotationPresent(AnnotationNew.class)) {//Смотрим, есть ли у метода нужная нам Аннотация
                AnnotationNew ant = met.getAnnotation(AnnotationNew.class);//Берем объект нашей Аннотации
                map.put(ant.name(), met);//Кладем в качестве ключа нашей карты параметр name() и метод
            }
        }
    }

    @AnnotationNew(name = "clear")//
    public static void clear(String[] args) {
        if (Main.answer.equals("\n")) Main.saveHistory.add("clear");
        if (args.length == 0) {
            Main.collection.clear();
            Main.answer = Main.answer + "Коллекция была очищена\n";

        }else Main.answer = Main.answer + "У этой команды нет аргумента.\n";
    }

    @AnnotationNew(name = "help")//
    public static void help(String[] args){
        if (Main.answer.equals("\n")) Main.saveHistory.add("help");
        if (args.length == 0) {
            Main.answer = Main.answer + """
                    help : вывести справку по доступным командам
                    info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)
                    show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении
                    add {element} : добавить новый элемент в коллекцию
                    update id {element} : обновить значение элемента коллекции, id которого равен заданному
                    remove_by_id id : удалить элемент из коллекции по его id
                    clear : очистить коллекцию
                    save : сохранить коллекцию в файл
                    execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.
                    exit : завершить программу (без сохранения в файл)
                    head : вывести первый элемент коллекции
                    remove_head : вывести первый элемент коллекции и удалить его
                    history : вывести последние 15 команд (без их аргументов)
                    remove_any_by_students_count studentsCount : удалить из коллекции один элемент, значение поля studentsCount которого эквивалентно заданному
                    average_of_students_count : вывести среднее значение поля studentsCount для всех элементов коллекции
                    filter_by_form_of_education formOfEducation : вывести элементы, значение поля formOfEducation которых равно заданному \n""";
        }else Main.answer = Main.answer + "У этой команды нет аргумента.\n";

    }

    @AnnotationNew(name = "info")//
    public static void info(String[] args){
        if (Main.answer.equals("\n")) Main.saveHistory.add("info");
        if (args.length == 0) {
            Main.answer = Main.answer + " Информация о коллекции:\n" +
                    "Тип коллекции - PriorityQueue\n" +
                    "Дата инициализации - " + LocalDate.now() + "\n" +
                    "Количество эллементов - " + Main.collection.size() + "\n";

        }else Main.answer = Main.answer + "У этой команды нет аргумента.\n";
    }

    @AnnotationNew(name = "show")//
    public static void show(String[] args){
        if (Main.answer.equals("\n")) Main.saveHistory.add("show");
        if (args.length == 0) {
            int sizeOfCollection = Main.collection.size();
            if (sizeOfCollection == 0) {
                Main.answer = Main.answer + "В коллекции нет объектов.\n";
            } else {
                ArrayList<String> msg = new ArrayList<>();
                Main.collection.forEach(studyGroup -> msg.add(studyGroup.toString()));
                msg.forEach(s -> Main.answer = Main.answer+s);
                Main.collection.addAll(Main.twoColl);
                Main.twoColl.clear();
            }
        }else Main.answer = Main.answer + "У этой команды нет аргумента.\n";
    }

    @AnnotationNew(name = "add")//
    public static void add(String[] args)  {
        if (Main.answer.equals("\n")) Main.saveHistory.add("add");
        if (args.length == 0) {
            addCommand addCommand = (addCommand)Main.object;
            long id;
            Random random = new Random();
            id = (random.nextInt(1000) + 1);
            while (Main.AllId.contains(id)) {
                id = (random.nextInt(1000) + 1);
            }
            Main.AllId.add(id);
            Date date = new Date();

            Coordinates coordinates = new Coordinates(addCommand.x, addCommand.y);
            Person person = new Person(addCommand.nameOfGroup, addCommand.passportID, addCommand.eyeColor, addCommand.hairColor, addCommand.nationality);
            StudyGroup studyGroup = new StudyGroup(id, addCommand.nameOfGroup, coordinates, date, addCommand.studentsCount, addCommand.formOfEducation, addCommand.semesterEnum, person);

            Main.collection.add(studyGroup);
            Main.answer = Main.answer + "Объект был успешно добавлен в коллекцию.\n";

        }else Main.answer = Main.answer + "У этой команды нет аргумента.\n";
    }

    @AnnotationNew(name = "update")//
    public static void update(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        if (Main.answer.equals("\n")) {
            Main.saveHistory.add("update");
        }

        if (args.length==1 && Pattern.compile("\\d+").matcher(args[0]).matches()) {
            int sizeOfCollection = Main.collection.size();
            Optional<StudyGroup> optionalStudyGroup = Main.collection.stream().filter(studyGroup -> Objects.equals(studyGroup.getId(), Long.valueOf(args[0]))).findFirst();
            StudyGroup studyGroupNew = optionalStudyGroup.orElse(null);
            if (sizeOfCollection == 0) {
                Main.answer = Main.answer + "В коллекции нет объектов.\n";
            } else {
                if (studyGroupNew == null) {
                    Main.answer = Main.answer + "В коллекции нет такого эллемента с данным iD:" + args[0] + "\n";
                } else {
                    Main.collection.remove(studyGroupNew);
                    String an = "Замените эллемент.";
                    Main.message = new Message(an);

                    InetAddress host = inputPacket.getAddress();
                    int senderPort = inputPacket.getPort();
                    sendingDataBuffer = serialize(message);
                    DatagramPacket outputPacket = new DatagramPacket(sendingDataBuffer, sendingDataBuffer.length, host, senderPort);
                    datagramSocket.send(outputPacket);// Отправляем пакет клиенту
                    System.out.println("Сообщение о результате выполненной команды было отправлено на клиент.");
                    Thread.sleep(300);

                    receivingDataBuffer = new byte[16384];
                    inputPacket = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length);//экземпляр UDP-пакета для хранения клиентских данных с использованием буфера для полученных данных
                    System.out.println("Ждём действий от клиента...");
                    datagramSocket.receive(inputPacket);// получаем данные от клиента и сохраните их в datagramPacket
                    System.out.println("Был получен объект. Идёт обработка... ");
                    Thread.sleep(300);

                    object = (AbstractCommand) deserialize(receivingDataBuffer);
                    object.execute();
                    System.out.println("Идёт выпеполнение команды...");
                    Thread.sleep(300);

                    Main.answer = Main.answer + "Объект удачно изменён\n";
                }
            }
        }else Main.answer = Main.answer + "У этой команды должен быть один аргумент.\n";
    }

    @AnnotationNew(name = "remove_by_id")//
    public static void remove_by_id(String[] args){
        if (Main.answer.equals("\n")) Main.saveHistory.add("remove_by_id");
        if (args.length == 1 && Pattern.compile("\\d+").matcher(args[0]).matches()) {
            int sizeOfCollection = Main.collection.size();
            Optional<StudyGroup> optionalStudyGroup = Main.collection.stream().filter(studyGroup -> Objects.equals(studyGroup.getId(), Long.valueOf(args[0]))).findFirst();
            StudyGroup studyGroupNew = optionalStudyGroup.orElse(null);

            if (sizeOfCollection == 0) {
                Main.answer = Main.answer + "В коллекции нет объектов.\n";
            } else {
                if (studyGroupNew == null) {
                    Main.answer = Main.answer + "В коллекции нет такого эллемента с данным iD:" + args[0] + "\n";
                } else {
                    Main.collection.remove(studyGroupNew);
                    Main.answer = Main.answer + "Объект с iD: " + args[0] + "был удалён.\n";
                }

            }
        }else Main.answer = Main.answer + "У этой команды должен быть один аргумент.\n";
    }

    @AnnotationNew(name = "execute_script")//
    public static void execute_script(String[] args){
        if (Main.answer.equals("\n")) Main.saveHistory.add("execute_script");
        if (args.length == 1 && Pattern.compile("^[A-Za-z0-9+_.-]+\\.[a-zA-Z0-9]+$").matcher(args[0]).matches()) {
            try {
                Main.answer = "";
                String nameOfFile = args[0];
                File file = new File(nameOfFile);

                if (file.exists()) {

                    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        String[] strSplit = line.split(" ");
                        String strCommand = strSplit[0].toLowerCase();//Разделяю первое слово от остальных
                        String[] strArguments = Arrays.copyOfRange(strSplit, 1, strSplit.length);

                        Main.answer = Main.answer + "\n" + strCommand + "\n";
                        Method met = map.get(strCommand);//(strCommand-первое слово в строке или команда)
                        //System.out.println(strCommand);
                        String str = "execute_script";
                        String str1 = "add";
                        if (!strCommand.equals(str1.toLowerCase())) {
                            if (strCommand.equals(str.toLowerCase()) && strArguments.length == 1 && strArguments[0].equals(nameOfFile)) {

                                Main.answer = Main.answer + "В файле есть команда execute_script с таким же именем файла, который был введен ранее.\n" +
                                        "Эта команда не была реализована, потому что это приведёт к бесконечному повтору команд из файла. \n";

                            } else met.invoke(com, (Object) strArguments);
                        } else {
                            String an = "Замените эллемент.";
                            Main.message = new Message(an);

                            InetAddress host = inputPacket.getAddress();
                            int senderPort = inputPacket.getPort();
                            sendingDataBuffer = serialize(message);
                            DatagramPacket outputPacket = new DatagramPacket(sendingDataBuffer, sendingDataBuffer.length, host, senderPort);
                            datagramSocket.send(outputPacket);// Отправляем пакет клиенту
                            System.out.println("Сообщение о результате выполненной команды было отправлено на клиент.");
                            Thread.sleep(300);

                            receivingDataBuffer = new byte[16384];
                            inputPacket = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length);//экземпляр UDP-пакета для хранения клиентских данных с использованием буфера для полученных данных
                            System.out.println("Ждём действий от клиента...");
                            datagramSocket.receive(inputPacket);// получаем данные от клиента и сохраните их в datagramPacket
                            System.out.println("Был получен объект. Идёт обработка... ");
                            Thread.sleep(300);

                            object = (AbstractCommand) deserialize(receivingDataBuffer);
                            object.execute();
                            System.out.println("Идёт выпеполнение команды...");
                            Thread.sleep(300);

                            Main.answer = Main.answer + "Объект удачно изменён\n";
                        }
                    }
                    bufferedReader.close();
                } else Main.answer = "Такого файла не существует.";

            } catch (IOException | InvocationTargetException | IllegalAccessException | ClassNotFoundException | InterruptedException e) {
                Main.answer = "В файле неправильные данные команд.";
            }
        }else Main.answer = Main.answer + "У команды execute_script должен быть один аргумент, который являеться названием файла.\n";
    }

    @AnnotationNew(name = "head")//
    public static void head(String[] args){
        if (Main.answer.equals("\n")) Main.saveHistory.add("head");
        if (args.length == 0) {
            if (Main.collection.size() != 0) {
                Main.answer = Main.answer + Main.collection.stream().findFirst().get() + "\n";
            } else Main.answer = Main.answer + "В коллекции нет объектов.\n";
        }else Main.answer = Main.answer + "У этой команды нет аргумента.\n";
    }

    @AnnotationNew(name = "remove_head")//
    public static void remove_head(String[] args) {
        if (Main.answer.equals("\n")) Main.saveHistory.add("remove_head");
        if (args.length == 0) {
            if (Main.collection.size() != 0) {
                StudyGroup example = Main.collection.poll();
                if (example != null) {Main.answer = Main.answer + example;}
            } else Main.answer = Main.answer + ("В коллекции нет эллементов.");
        }else Main.answer = Main.answer + "У этой команды нет аргумента.\n";
    }

    @AnnotationNew(name = "remove_any_by_students_count")//777
    public static void remove_any_by_students_count(String[] args){
        if (Main.answer.equals("\n")) Main.saveHistory.add("remove_any_by_students_count");
        if ((args.length == 1) && (Pattern.compile("\\d+").matcher(args[0]).matches())) {

            int sizeOfCollection = Main.collection.size();

            if (sizeOfCollection == 0) {
                Main.answer = Main.answer + "В коллекции нет объектов.\n";
            }
            StudyGroup studyGroup1 = Main.collection.stream()
                    .filter(studyGroup -> studyGroup.getStudentsCount() == Long.parseLong(args[0]))
                    .findFirst().get();
            Main.collection.remove(studyGroup1);
        }else Main.answer = Main.answer + "У этой команды нет аргумента.\n";
    }

    @AnnotationNew(name = "average_of_students_count")//работает
    public static void average_of_students_count(String[] args){
        if (Main.answer.equals("\n")) Main.saveHistory.add("average_of_students_count");
        if (args.length == 0) {
            int sizeOfCollection = Main.collection.size();
            if (sizeOfCollection == 0) {
                Main.answer = Main.answer + "В коллекции нет объектов.";
            } else {

                ArrayList<Long> summ = new ArrayList<>();
                Main.collection.forEach(studyGroup -> summ.add(studyGroup.getStudentsCount()) );
                Long s = null;
                Long count = null;
                for(Long l: summ) {
                    s += l;
                    count+=1;
                }
                Main.answer = Main.answer + "Среднее значение колличества студентов = " + s/count;
            }
        }else Main.answer = Main.answer + "У этой команды нет аргумента.\n";
    }

    @AnnotationNew(name = "filter_by_form_of_education")//
    public static void filter_by_form_of_education(String[] args){
        if (Main.answer.equals("\n")) Main.saveHistory.add("filter_by_form_of_education");
        if ((args.length == 1)&&(args[0].equals("FULL_TIME_EDUCATION") || args[0].equals("DISTANCE_EDUCATION") || args[0].equals("EVENING_CLASSES"))){
            int sizeOfCollection = Main.collection.size();

            if (sizeOfCollection == 0) {
                Main.answer = Main.answer + "В коллекции нет объектов.\n";
            }

            Main.collection.stream().filter(studyGroup -> FormOfEducation.valueOf(args[0]) == studyGroup.getFormOfEducation()).forEach(studyGroup -> Main.answer = Main.answer + studyGroup + "\n");
            Main.collection.addAll(Main.twoColl);
            Main.twoColl.clear();
        }else Main.answer = Main.answer + "У этой команды должен быть один аргумент.\n";
    }

    @AnnotationNew(name = "history")//
    public static void history(String[] args) {
        if (Main.answer.equals("\n")) Main.saveHistory.add("history");
        if (args.length == 0) {
            int size = Main.saveHistory.size();
            if (size != 0) {
                if (size < 15) {

                    Main.answer = Main.answer + "Вы ввели меньше 15 команд до этого. Часть команд, которая была введена: " + "\n" +
                            Main.saveHistory.subList(0, size-1) + "\n";

                } else Main.answer = Main.answer + "Последние введённые 15 команд:" + "\n" +
                        Main.saveHistory.subList(size - 14, size) + "\n";

            } else Main.answer = Main.answer + "Ранее не было введено ни одной команды. " + "\n";
        } else Main.answer = Main.answer + "У этой команды нет аргумента.\n";
    }
}

