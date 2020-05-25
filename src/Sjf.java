import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

public class Sjf implements Method{
	
	private List<Integer> responseTimes;
    private List<Integer> returnTimes;
    
    public Sjf() {
    	responseTimes = new ArrayList<>();
        returnTimes = new ArrayList<>();
    }

	@Override
	public int start(List<Client> list, LocalTime dayStart, LocalTime dayEnd) {
		ClientSorter.sortByEstimated(list);
	       LocalTime actual = dayStart;
	        int clientsFinalized = 0;
			
	        for (Client client : list) {
	            LocalTime startedAt = actual;
	            LocalTime finalizeAt = startedAt.plusHours(client.getEstimatedTime().getHour()).plusMinutes(client.getEstimatedTime().getMinute());

	            if (finalizeAt.isBefore(dayEnd)) {
	               //System.out.println("Started at: " + startedAt + "\t|\t" + "Password: " + client.getCode() + "\t|\tFinalized at: " + finalizeAt);
	                actual = finalizeAt;
	                clientsFinalized++;
	               //System.out.println("startedAt.getMinute() = " + ((startedAt.getMinute() - dayStart.getMinute()) + (startedAt.getHour() - dayStart.getHour())*60  ));
	                responseTimes.add(
	                        (
	                                (
	                                        startedAt.getMinute() - dayStart.getMinute()
	                                ) + (
	                                        startedAt.getHour() - dayStart.getHour()
	                                ) * 60
	                        )
	                );
	                returnTimes.add(
	                        (
	                                (
	                                        finalizeAt.getMinute() - dayStart.getMinute()
	                                ) + (
	                                        finalizeAt.getHour() - dayStart.getHour()
	                                ) * 60
	                        )
	                );
	            }
	        }
			
			return clientsFinalized;
	}

	@Override
	public double getResponseTime() {
		OptionalDouble average = responseTimes.stream().mapToInt(Integer::valueOf).average();
        if (average.isPresent()) return average.getAsDouble();
		return 0;
	}

	@Override
	public double getReturnTime() {
		OptionalDouble average = returnTimes.stream().mapToInt(Integer::valueOf).average();
        if (average.isPresent()) return average.getAsDouble();
		return 0;
	}

}