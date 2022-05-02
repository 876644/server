package com.company.Commands;


import com.company.Main;

public class remove_by_idCommand extends AbstractCommand {
    public Long ID;
    private static final long serialVersionUID = 11;
    @Override
    public void execute() {
        remove_by_idCommand r = (remove_by_idCommand) Main.object;
        String[] a = new String[1];
        a[0]= String.valueOf(r.ID);
        AllCommands.remove_by_id(a);
    }

    public remove_by_idCommand(Long ID) {
        this.ID = ID;
    }
}
