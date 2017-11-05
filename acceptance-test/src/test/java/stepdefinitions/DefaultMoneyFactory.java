package stepdefinitions;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DefaultMoneyFactory extends DefaultFactory {
    @Override
    public Map<String, List<Object>> getSkeleton(Map<String, Object> moneyEntityBundle ) {


        Map<String, Object> amount = new HashMap<>();
        Map<String, Object> moneyEntity = new HashMap<>();
        ArrayList<Object> moneyEntities = new ArrayList<>();
        moneyEntities.add(moneyEntity);

        Map<String, List<Object>> skeleton = new HashMap<>();
        skeleton.put("reason", ImmutableList.of(moneyEntityBundle,"wat"));
        skeleton.put(getEntityName(), ImmutableList.of(moneyEntityBundle,moneyEntities));
        skeleton.put("name", ImmutableList.of(moneyEntity,"Bob Dylan"));
        skeleton.put("mobilePhone", ImmutableList.of(moneyEntity,"685551441"));
        skeleton.put("email", ImmutableList.of(moneyEntity,"bob@dylan.com"));
        skeleton.put("accountNumber", ImmutableList.of(moneyEntity,"2423452345234523"));
        skeleton.put("unknownProperty", ImmutableList.of(moneyEntity,"aosdaoisd"));
        skeleton.put("amount", ImmutableList.of(moneyEntity,amount));
        skeleton.put("currency", ImmutableList.of(amount,"EUR"));
        skeleton.put("value", ImmutableList.of(amount,10));

        return skeleton;
    }

    abstract public String getEntityName();

}
