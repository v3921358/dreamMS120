/*
 * Time Temple - Kirston
 * Twilight of the Gods
 */

function start() {
    cm.askAcceptDecline("如果只有我有能力，那麼我可以重新召喚黑巫師! \r\n等等！事情不對！為什麼黑巫師不會召喚？等等，這是什麼光環？我感覺到與黑巫師完全不同的東西!!!!! \r\n\r\n #b(把手放在大G的牙籤上.)");
}

function action(mode, type, selection) {
    if (mode == 1) {
	cm.removeNpc(270050100, 2141000);
	cm.forceStartReactor(270050100, 2709000);
    }
    cm.dispose();

// If accepted, = summon PB + Kriston Disappear + 1 hour timer
// If deny = NoTHING HAPPEN
}