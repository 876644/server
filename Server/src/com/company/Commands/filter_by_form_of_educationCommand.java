package com.company.Commands;

import com.company.Main;
import com.company.classes.StudyGroup;

public class filter_by_form_of_educationCommand extends AbstractCommand {
    public String formOfEducation;

    public filter_by_form_of_educationCommand(String formOfEducation) {
        this.formOfEducation = formOfEducation;
    }

    private static final long serialVersionUID = 6;

    @Override
    public void execute() {
        filter_by_form_of_educationCommand filter_by_form_of_educationCommand = (filter_by_form_of_educationCommand)Main.object;
        String[] a = new String[1];
        a[0]=filter_by_form_of_educationCommand.formOfEducation;
        AllCommands.filter_by_form_of_education(a);
    }
}

