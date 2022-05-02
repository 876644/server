package com.company.Commands;

import com.company.Enums.*;
import com.company.Main;

public class addCommand extends AbstractCommand {
    public String nameOfGroup;
    public Double x;
    public Integer y;
    public Long studentsCount;
    public FormOfEducation formOfEducation;
    public Semester semesterEnum;
    public String namePerson;
    public String passportID;
    public ColorEye eyeColor;
    public ColorHair hairColor;
    public Country nationality;

    private static final long serialVersionUID = 3;

    public addCommand(String nameOfGroup, Double x, Integer y, Long studentsCount, FormOfEducation formOfEducation, Semester semesterEnum, String namePerson, String passportID, ColorEye eyeColor, ColorHair hairColor, Country nationality) {
        this.nameOfGroup = nameOfGroup;
        this.x = x;
        this.y = y;
        this.studentsCount = studentsCount;
        this.formOfEducation = formOfEducation;
        this.semesterEnum = semesterEnum;
        this.namePerson = namePerson;
        this.passportID = passportID;
        this.eyeColor = eyeColor;
        this.hairColor = hairColor;
        this.nationality = nationality;
    }

    @Override
    public void execute() {
        String[] a = new String[0];
        AllCommands.add(a);
    }

}
