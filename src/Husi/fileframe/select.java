package Husi.fileframe;

public enum select {

    A("節A",0),
    B("節B",1),
    C("節C",2),
    D("節D",3),
    H("振れ幅",4),
    I("差",5);


    private String label;
    private int id;

    private select(String label, int id) {
        this.label = label;
        this.id = id;
    }


    public String getLabel() {
        return label;
    }

    public static int getselectId(String name) {
        for(select sel : select.values()){
            if(name==sel.label){
                return sel.id;
            }
        }
        return 0;
    }
    public static String getlabel(select sel){
        String str = sel.label;
        return str;
    }

    public static int getid(select sel){
        int id  = sel.id;
        return id;
    }
    
    
}
