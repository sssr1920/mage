package mage.cards.m;

import mage.abilities.Ability;
import mage.abilities.common.EntersBattlefieldTappedAbility;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.costs.common.SacrificeSourceCost;
import mage.abilities.costs.common.TapSourceCost;
import mage.abilities.costs.mana.GenericManaCost;
import mage.abilities.effects.Effect;
import mage.abilities.effects.common.search.SearchLibraryPutInPlayEffect;
import mage.abilities.mana.ColorlessManaAbility;
import mage.cards.Card;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.cards.Cards;
import mage.constants.CardType;
import mage.constants.SubType;
import mage.constants.SuperType;
import mage.constants.Zone;
import mage.filter.FilterCard;
import mage.filter.predicate.Predicates;
import mage.game.Game;
import mage.target.common.TargetCardInLibrary;
import mage.util.SubTypeList;

import java.util.Iterator;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author LevelX2
 */
public final class MyriadLandscape extends CardImpl {

    public MyriadLandscape(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.LAND}, "");

        // Myriad Landscape enters the battlefield tapped.
        this.addAbility(new EntersBattlefieldTappedAbility());

        // {tap}: Add {C}.
        this.addAbility(new ColorlessManaAbility());

        // {2}, {tap}, Sacrifice Myriad Landscape: Search your library for up to two basic land cards that share a land type, put them onto the battlefield tapped, then shuffle your library.
        Effect effect = new SearchLibraryPutInPlayEffect(new TargetCardInLibrarySharingLandType(0, 2), true);
        effect.setText("Search your library for up to two basic land cards that share a land type, put them onto the battlefield tapped, then shuffle your library");
        Ability ability = new SimpleActivatedAbility(Zone.BATTLEFIELD, effect, new GenericManaCost(2));
        ability.addCost(new TapSourceCost());
        ability.addCost(new SacrificeSourceCost());
        this.addAbility(ability);

    }

    public MyriadLandscape(final MyriadLandscape card) {
        super(card);
    }

    @Override
    public MyriadLandscape copy() {
        return new MyriadLandscape(this);
    }
}

class TargetCardInLibrarySharingLandType extends TargetCardInLibrary {

    private static final FilterCard filterBasicLandCard = new FilterCard("basic land card");

    static {
        filterBasicLandCard.add(Predicates.or(
                SubType.getLandTypes()
                        .stream()
                        .map(SubType::getPredicate)
                        .collect(Collectors.toSet())
        ));
        filterBasicLandCard.add(SuperType.BASIC.getPredicate());
    }

    public TargetCardInLibrarySharingLandType(int minNumTargets, int maxNumTargets) {
        super(minNumTargets, maxNumTargets, filterBasicLandCard);
    }

    public TargetCardInLibrarySharingLandType(final TargetCardInLibrarySharingLandType target) {
        super(target);
    }

    @Override
    public boolean canTarget(UUID playerId, UUID id, Ability source, Cards cards, Game game) {
        if (super.canTarget(playerId, id, source, cards, game)) {
            if (!getTargets().isEmpty()) {
                // check if new target shares a Land Type
                SubTypeList landTypes = new SubTypeList();
                for (UUID landId : getTargets()) {
                    Card landCard = game.getCard(landId);
                    if (landCard != null) {
                        if (landTypes.isEmpty()) {
                            landTypes.addAll(landCard.getSubtype(game));
                        } else {
                            landTypes.removeIf(next -> !landCard.hasSubtype(next, game));
                        }
                    }
                }
                Card card = game.getCard(id);
                if (card != null && !landTypes.isEmpty()) {
                    for (Iterator<SubType> iterator = landTypes.iterator(); iterator.hasNext(); ) {
                        SubType next = iterator.next();
                        if (card.hasSubtype(next, game)) {
                            return true;
                        }
                    }
                }
            } else {
                // first target
                return true;
            }
        }
        return false;
    }

    @Override
    public TargetCardInLibrarySharingLandType copy() {
        return new TargetCardInLibrarySharingLandType(this);
    }

}
