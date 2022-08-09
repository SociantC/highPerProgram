// let invoice;
// let plays;
function statement(invoice1, plays1) {

    return renderPlainText(createStatementData(statement, plays1));


}

function createStatementData(invoice,plays){
    const statement = {};
    statement.customer = invoice1.customer;
    statement.performance = invoice1.performance.map(enrichPerformance);
    statement.totalAmount = totalAmount(statement);
    statement.totalVolumeCredits = totalVolumeCredits(statement);
    return statement;
}

function totalAmount(data) {
    return data.performance.reduce((total, p) => total + p.amount, 0);
}
function totalVolumeCredits(data) {
    return data.performance.reduce((total, p) => total + p.volumeCredits, 0);

}
function enrichPerformance(aPerformance){
    const result = Object.assign({}, aPerformance);
    result.play = playFor(result);
    result.amount = amountFor(result);
    result.volumeCredits = volumeCreditsFor(result);
    return result;
}

function renderPlainText(data, plays) {
    let result = `Statement for ${data.customer}\n`;
    for (let perf of data.performance) {
        result += ` ${perf.play.name}:${usd(perf.amount)} (${perf.audience} seats)\n`;
    }
    result += `Amount owed is ${usd(data.totalAmount)}\n`;
    result += `You earned ${data.totalVolumeCredits} credits \n`;
    return result;
}
function usd(aNumber) {
    return new Intl.NumberFormat("en-US", {
        style: "currency",
        currency: "USD",
        minimumFractionDigits: 2
    }).format(aNumber / 100);
}
function playFor(aPerformance) {
    return plays[aPerformance.playId];
}
function amountFor(aPerformance) {
    let result = 0;
    switch (playFor(aPerformance).type) {
        case "tragedy":
            result = 40000;
            if (aPerformance.audience > 30) {
                result += 1000 * (aPerformance.audience - 30);
            }
            break;
        case "comedy":
            result = 30000;
            if (aPerformance.audience > 20) {
                result += 10000 + 500 * (aPerformance.audience - 20);
            }
            result += 300 * aPerformance.audience;
            break;
        default:
            throw new Error(`unknown type:${playFor(aPerformance).type}`);
    }
    return result;
}
function volumeCreditsFor(perf) {
    let result = 0;
    result += Math.max(perf.audience - 30, 0);
    if ("comedy" === playFor(perf).type) result += Math.floor(perf.audience / 5);
    return result;
}

