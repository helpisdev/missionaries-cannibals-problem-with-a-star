package gr.kapareliotis.ilias.missionariesCannibals;

import java.util.HashMap;

public enum RiverBank {
    LEFT(0),
    RIGHT(1);

    private static final HashMap<Integer, RiverBank> valueToRiverBank = new HashMap<>();

    static {
        for (RiverBank riverBank : RiverBank.values()) {
            RiverBank.valueToRiverBank.put(riverBank.getBankValue(), riverBank);
        }
    }

    private final int bankValue;

    RiverBank(int bankValue) {
        this.bankValue = bankValue;
    }

    public static RiverBank getBank(int bankValue) {
        return RiverBank.valueToRiverBank.get(bankValue);
    }

    public int getBankValue() {
        return this.bankValue;
    }
}
