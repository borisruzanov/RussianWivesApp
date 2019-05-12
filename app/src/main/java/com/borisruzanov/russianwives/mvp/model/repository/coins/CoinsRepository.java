package com.borisruzanov.russianwives.mvp.model.repository.coins;

import com.borisruzanov.russianwives.utils.BoolCallback;
import com.borisruzanov.russianwives.utils.Consts;
import com.borisruzanov.russianwives.utils.NumCallback;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.borisruzanov.russianwives.utils.FirebaseUtils.getUid;

public class CoinsRepository {

    private CollectionReference users = FirebaseFirestore.getInstance().collection(Consts.USERS_DB);

    public void addCoins(String uid, int addCoins) {
        if (uid != null) {
            users.document(uid).get().addOnCompleteListener(task -> {
                DocumentSnapshot snapshot = task.getResult();
                int coins = snapshot.getLong(Consts.COINS).intValue();
                int newCoins = coins + addCoins;
                Map<String, Object> achMap = new HashMap<>();
                achMap.put(Consts.COINS, newCoins);
                users.document(uid).update(achMap);
            });
        }
    }

    public void addCoins(int addCoins) {
        addCoins(getUid(), addCoins);
    }

    public void deleteCoins(int delCoins, BoolCallback enoughCallback) {
        deleteCoins(getUid(), delCoins, enoughCallback);
    }

    public void hasEnoughCoins(int needCoins, BoolCallback enoughCallback) {
        if (getUid() != null) {
            users.document(getUid()).get().addOnCompleteListener(task -> {
                DocumentSnapshot snapshot = task.getResult();
                int coins = snapshot.getLong(Consts.COINS).intValue();
                int newCoins = coins - needCoins;
                // if user has enough coins value will be higher than 0
                enoughCallback.setBool(newCoins > 0);
            });
        }
    }

    private void deleteCoins(String uid, int delCoins, BoolCallback enoughCallback) {
        if (uid != null) {
            users.document(uid).get().addOnCompleteListener(task -> {
                DocumentSnapshot snapshot = task.getResult();
                int coins = snapshot.getLong(Consts.COINS).intValue();
                int newCoins = coins - delCoins;
                if (newCoins > 0) {
                    Map<String, Object> achMap = new HashMap<>();
                    achMap.put(Consts.COINS, newCoins);
                    users.document(uid).update(achMap);
                    enoughCallback.setBool(true);
                } else enoughCallback.setBool(false);
            });
        }
    }

    public void getCoins(NumCallback callback) {
        getCoins(getUid(), callback);
    }

    public void getCoins(String uid, NumCallback numCallback) {
        if (uid != null) {
            users.document(uid).get().addOnCompleteListener(task -> {
                DocumentSnapshot snapshot = task.getResult();
                int coins = snapshot.getLong(Consts.COINS).intValue();
                numCallback.setNumber(coins);
            });
        }
    }

}
