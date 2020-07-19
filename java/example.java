import xroom.JavaAPI;

public class example
{
    public static void main(String args[]) throws Exception
    {
        JavaAPI API = new JavaAPI("YOUR_USERNAME", "YOUR_SECRET");

        String result = API.Exec ("room", "init", "{\"id\":\"your-domain.com/hello-world\"}");
        System.out.println (result);
    }
}
