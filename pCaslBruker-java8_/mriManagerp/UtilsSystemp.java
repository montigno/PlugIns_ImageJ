package mriManagerp;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.security.CodeSource;
 
public class UtilsSystemp {
 
  /**
   * Return the absolute path of the jar (Java 1.6 min)
   *
   * @return The path of the jar
   */
  public static String pathOfJar()
  {
    CodeSource codeSource;
    String jarDir = "";
    File jarFile;
 
    try
    {
      codeSource = UtilsSystemp.class.getProtectionDomain().getCodeSource();
      jarFile = new File(URLDecoder.decode(codeSource.getLocation().toURI().getPath(), "UTF-8"));
      jarDir = jarFile.getParentFile().getPath() + File.separator;
    }
    catch (URISyntaxException e)
    {
    	new GetStackTracep(e);
		PcaslBruker_.getBugText().setText(PcaslBruker_.getBugText().getText()+"\n----------------\n"+GetStackTracep.getMessage());
    }
    catch (UnsupportedEncodingException e)
    {
    	new GetStackTracep(e);
		PcaslBruker_.getBugText().setText(PcaslBruker_.getBugText().getText()+"\n----------------\n"+GetStackTracep.getMessage());
    }
 
    return jarDir;
  }
 
}