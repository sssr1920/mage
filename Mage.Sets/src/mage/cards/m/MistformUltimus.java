package mage.cards.m;

import mage.MageInt;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.effects.common.InfoEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.SubType;
import mage.constants.SuperType;
import mage.constants.Zone;

import java.util.UUID;

/**
 * @author vereena42
 */
public final class MistformUltimus extends CardImpl {

    public MistformUltimus(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.CREATURE}, "{3}{U}");

        this.addSuperType(SuperType.LEGENDARY);
        this.subtype.add(SubType.ILLUSION);
        this.power = new MageInt(3);
        this.toughness = new MageInt(3);

        // Mistform Ultimus is every creature type.
        this.setIsAllCreatureTypes(true);
        this.addAbility(new SimpleStaticAbility(Zone.ALL, new InfoEffect(
                "{this} is every creature type <i>(even if this card isn't on the battlefield)</i>."
        )));
    }

    public MistformUltimus(final MistformUltimus card) {
        super(card);
    }

    @Override
    public MistformUltimus copy() {
        return new MistformUltimus(this);
    }
}
