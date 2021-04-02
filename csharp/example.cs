using System;
using xroom;

namespace example
{
    class MainClass
    {
        public static void Main (string[] args)
        {
            var API = new CSharpAPI ("YOUR_PUBLIC_KEY", "YOUR_PRIVATE_KEY");

            string result = API.Exec ("room", "init", "{\"id\":\"your-domain.com/hello-world\"}");
            Console.WriteLine (result);
        }
    }
}
