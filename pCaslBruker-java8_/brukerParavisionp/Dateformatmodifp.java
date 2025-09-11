package brukerParavisionp;
public class Dateformatmodifp {

	private String rdate="";
	private int version;
	
	public Dateformatmodifp (String rdate, int version){
		
		this.rdate=rdate;
		this.version=version;
		
	}

	public String newformdate () {
		
		String year="",mounth="",day="",hour="";
		
		if (version == 1) {
	
			hour=rdate.substring(0,rdate.indexOf(" "));
			rdate=rdate.substring(rdate.indexOf(" ")+1);
			day=rdate.substring(0,2);
			day=day.replace(" ","0");
			rdate=rdate.substring(3);
			//day=rdate.substring(0,rdate.indexOf(" "));
			//rdate=rdate.substring(rdate.indexOf(" ")+1);
			mounth=rdate.substring(0,rdate.indexOf(" "));
			year=rdate.substring(rdate.indexOf(" ")+1);
			
			
			
			if (mounth.contentEquals("Jan")) mounth="01";
			else if (mounth.contentEquals("Feb")) mounth="02";
			else if (mounth.contentEquals("Mar")) mounth="03";
			else if (mounth.contentEquals("Apr")) mounth="04";
			else if (mounth.contentEquals("May")) mounth="05";
			else if (mounth.contentEquals("Jun")) mounth="06";
			else if (mounth.contentEquals("Jul")) mounth="07";
			else if (mounth.contentEquals("Aug")) mounth="08";
			else if (mounth.contentEquals("Sep")) mounth="09";
			else if (mounth.contentEquals("Oct")) mounth="10";
			else if (mounth.contentEquals("Nov")) mounth="11";
			else if (mounth.contentEquals("Dec")) mounth="12";
			
			rdate= year+"-"+mounth+"-"+day+"  "+hour;
			
		}
		else {
			year=rdate.substring(0,rdate.indexOf("T"));
			rdate=rdate.substring(rdate.indexOf("T")+1);
			hour=rdate.substring(0,rdate.indexOf(","));
			rdate=year+"  "+hour;
		}
		
		return rdate;
	}
}
