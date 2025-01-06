package Husi.mainsrc;

public class maxandmin {
    inputResult inputresult = new inputResult();
    private  String minString = null;
    public  String getMinString() {
        return minString;
    }
    private  String maxString = null;
    public  String getMaxString() {
        return maxString;
    }

    public  void setString(String Filename){
        String outresult = null;
        double maxamplitude = 0;
        double minamplitude = 1000;
        String[] resultsplit;
        for(int i = 0 ;; i++){
            outresult = inputresult.outresult(Filename, i);
            if(outresult == null)
            {
                break;
            }
            resultsplit = outresult.split(":");
            if(maxamplitude<Double.parseDouble(resultsplit[1])){
                maxString = outresult;
                maxamplitude = Double.parseDouble(resultsplit[1]);
            }
            if(minamplitude > Double.parseDouble(resultsplit[1])){
                minString = outresult;
                minamplitude = Double.parseDouble(resultsplit[1]);
            }
        }

    }
}
