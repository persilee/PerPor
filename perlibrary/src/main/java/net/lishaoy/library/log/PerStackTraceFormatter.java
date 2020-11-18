package net.lishaoy.library.log;

public class PerStackTraceFormatter implements PerLogFormatter<StackTraceElement[]>{
    @Override
    public String format(StackTraceElement[] data) {

        StringBuffer sb = new StringBuffer(128);
        if (data == null || data.length ==0){
            return null;
        }else if (data.length == 1) {
            return "\t─ " + data.toString();
        }else {
            for (int i = 0; i < data.length; i++) {
                if (i == 0) sb.append("stackTrace:  \n");
                if (i != data.length -1) {
                    sb.append("\t├ ");
                    sb.append(data[i].toString());
                    sb.append("\n");
                }else {
                    sb.append("\t└ ");
                    sb.append(data[i].toString());
                }
            }
            return sb.toString();
        }
    }
}
