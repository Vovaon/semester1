package practice05.homework;
import java.util.Locale;
public class Task4DotaMatchStats {
    public static void main(String[] args) {
        String[] heroes = {"Pudge", "Invoker", "Juggernaut", "Crystal Maiden", "Earthshaker"};
        int[] kills = {8, 12, 10, 2, 4};
        int[] deaths = {6, 3, 2, 9, 5};
        int[] assists = {14, 9, 8, 18, 16};
        int[] netWorth = {14500, 21200, 24800, 9800, 12300};
 
        int players = heroes.length;
 
        int totalKills = 0;
        int totalNetWorth = 0;
        for (int i = 0; i < players; i++) {
            totalKills += kills[i];
            totalNetWorth += netWorth[i];
        }
 
        double[] kdaRatios = new double[players];
        int mvpIndex = 0;
        for (int i = 0; i < players; i++) {
            kdaRatios[i] = (double) (kills[i] + assists[i]) / Math.max(1, deaths[i]);
            if (kdaRatios[i] > kdaRatios[mvpIndex]) {
                mvpIndex = i;
            }
        }
 
        System.out.println("========================================================================");
        System.out.println("                        DOTA 2: POST-MATCH REPORT");
        System.out.println("========================================================================");
        System.out.printf(Locale.US, "%-17s| %-10s | %-9s | %-9s | %s%n",
                "Герой", "K / D / A", "Net Worth", "KDA Ratio", "Kill Participation");
        System.out.println("------------------------------------------------------------------------");
 
        for (int i = 0; i < players; i++) {
            String kda = kills[i] + " / " + deaths[i] + " / " + assists[i];
            String netWorthText = String.format(Locale.US, "%,d", netWorth[i]);
            double killParticipation = (double) (kills[i] + assists[i]) / totalKills * 100;
 
            System.out.printf(Locale.US, "%-17s| %-10s | %-9s | %-9.2f | %.2f%%%n",
                    heroes[i], kda, netWorthText, kdaRatios[i], killParticipation);
        }
 
        System.out.println("------------------------------------------------------------------------");
        System.out.printf(Locale.US, "Загальна кількість вбивств команди: %d%n", totalKills);
        System.out.printf(Locale.US, "Сумарний Net Worth команди:          %,d золота%n", totalNetWorth);
        System.out.printf(Locale.US, " НАЙКРАЩИЙ ГРАВЕЦЬ МАТЧУ (MVP):    %s (KDA: %.2f)%n",
                heroes[mvpIndex], kdaRatios[mvpIndex]);
        System.out.println("========================================================================");
    }

}
