package ngordnet;
import java.util.Collection;
import java.util.Iterator;

public class WordLengthProcessor implements YearlyRecordProcessor{
	public double process(YearlyRecord yearlyRecord){
		Iterator<String> stringIterator = yearlyRecord.words().iterator();


		Double total_length = 0.0;
		Double word_count = 0.0;
		String werd;
		int werd_count;
		while (stringIterator.hasNext()){
			werd = stringIterator.next();
			System.out.println(werd);
			werd_count = yearlyRecord.count(werd);
			total_length += werd.length() * werd_count;
			word_count += werd_count;
		}
			if (word_count == 0.0){
			return 0.0;
		}
		return  total_length / word_count;
	}
}