/*
changed by: Thur 14:15 workshop team 12
Members:
William Grimsey - 1270044
Even Guo - 1307884
Sophie Su - 1273360
 */

public class PlayingStrategyFactory {

    // singleton
    private static final PlayingStrategyFactory instance = new PlayingStrategyFactory();

    public static PlayingStrategyFactory getInstance(){
        return instance;
    }

    public PlayingStrategyFactory() {
    }

    /**
     * create strategy according to type
     * @param type strategy type
     * @param player player
     * @return strategy
     */
    public IPlayingStrategy getStrategy(String type,Player player){
        IPlayingStrategy s = switch (type) {
            case "human" -> new HumanStrategy(player);
            case "clever" -> new CleverStrategy(player);
            case "basic" -> new BasicStrategy(player);
            case "random" -> new RandomStrategy(player);
            default -> null;
        };
        assert s != null;
        return s;

    }
}
