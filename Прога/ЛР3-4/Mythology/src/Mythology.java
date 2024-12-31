import creature.enumeration.FriendlinessStatus;
import creature.enumeration.LifeStatus;
import creature.monster.Pudge;
import exception.PlaceIsToHighExeption;
import creature.impl.Piglet;
import creature.impl.Tigger;
import creature.impl.WinnieThePooh;
import creature.enumeration.GodType;
import creature.impl.Freyja;
import creature.god.GojoSatoru;
import creature.impl.Thor;
import creature.human.ChristopherRobin;
import creature.enumeration.World;
import place.sanctuary.ElvenSanctuary;
import place.shop.GnomesShop;

import java.io.PrintStream;

import  org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Mythology {

    private static final Logger logger = LogManager.getLogger(Mythology.class);

    public static void main(String[] args) throws PlaceIsToHighExeption {
        System.setOut(new PrintStream(System.out) {
            @Override
            public void println(String s) {
                logger.info(s);
            }
        });

        System.setErr(new PrintStream(System.err) {
            @Override
            public void println(String s) {
                logger.error(s);
            }
        });

        //animals
        Piglet piglet = new Piglet(
                "Пятачок",
                98,
                LifeStatus.ALIVE,
                28.7,
                "Поросёнок",
                "Розовый",
                36.7,
                FriendlinessStatus.FRIENDLY
        );

        WinnieThePooh winnieThePooh = new WinnieThePooh(
                "Винни Пух",
                98,
                LifeStatus.ALIVE,
                69.2,
                "Медведь",
                "Желтый",
                58.4,
                FriendlinessStatus.FRIENDLY
        );

        Tigger tigger = new Tigger(
                "Тигра",
                96,
                LifeStatus.ALIVE,
                47.6,
                "Тигр",
                "Оранжево-черный",
                34.5,
                FriendlinessStatus.FRIENDLY
        );

        //people
        ChristopherRobin christopherRobin = new ChristopherRobin(
                "Кристофер Робин",
                98,
                "Мальчик",
                147.5,
                42.4,
                24.7,
                LifeStatus.ALIVE
        );

        //gods
        GojoSatoru gojoSatoru = new GojoSatoru(
                "Сатору Годжо",
                28,
                LifeStatus.ALIVE,
                0,
                "Не в браке",
                "Маг",
                GodType.OTHER,
                "Шесть глаз",
                10000,
                "Особый ранг",
                25879

        );
        Thor thor = new Thor(
                "Тор",
                100000,
                LifeStatus.ALIVE,
                3,
                "В браке с Сиф",
                "Гром и молния",
                GodType.ASGARDS

        );
        Freyja freyja = new Freyja(
                "Фрея",
                100000,
                LifeStatus.ALIVE,
                2,
                "В браке с Одом",
                "Любовь и Урожай",
                GodType.VANS
        );

        //monster
        Pudge pudge = new Pudge(
                "Пудж",
                15,
                LifeStatus.ALIVE,
                World.ANOTHER,
                10000,
                993.8,
                4
        );

        //shop
        GnomesShop gnomesShop = new GnomesShop(
                "Лавка Брока",
                World.SVARTALHEIM,
                0,
                8,
                18,
                "Оружие"
        );

        //sanctuary
        ElvenSanctuary elvenSanctuary = new ElvenSanctuary(
                "Святилище светлых эльфов",
                World.ALFHEIM,
                0,
                "Молитва к Свету",
                "Не молиться Тьме"
        );

        System.out.println("Однажды между "
                + pudge.getPudgeName()
                + "ом и "
                + gojoSatoru.getGojoSatoruName()
                + " произошла легендарная битва...");

        pudge.PudgeIsHungry();
        gojoSatoru.useCharm();
        pudge.hungerLevelTooHigh();

        System.out.println(piglet.getPigletName()
                + " крайне испугался боя, "
                + "поэтому "
                + winnieThePooh.getWinniesName()
                + " и его друзья решили бежать в "
                + elvenSanctuary.getSanctuaryName());

        System.out.println("Однако внезапно перед ними оказался выскокий выступ"
                + " и все покатились со склона, превратишвшись в кучу мала");

        System.out.println("Первым пришел в себя и поднялся"
                + christopherRobin.getChristopherRobinName()
                + ". После он начала помогать своим друзьям"
                );

        christopherRobin.riseSomeoneUp(winnieThePooh.getWinniesName(), 40.7);
        winnieThePooh.riseSomeoneUp(piglet.getPigletName(), 25.3);
        piglet.riseSomeoneUp(tigger.getTiggerName(), 36.1);

        System.out.println("После вместе они достигли"
                + elvenSanctuary.getSanctuaryName()
                + " и спаслись!"
                + " а теперь вернемся к бою..."
        );

        System.out.println(gojoSatoru.getGojoSatoruName()
                + " наиспользует последнню атаку...");

        gojoSatoru.usePurple();
        System.out.println("И победителем, как всегда, "
                + gojoSatoru.getGojoSatoruName());

    }
}