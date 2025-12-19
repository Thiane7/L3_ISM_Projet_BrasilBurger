using Microsoft.AspNetCore.Mvc;
using brasil_burger.Models;
using brasil_burger.Data;
using System.Text.Json;

namespace brasil_burger.Controllers
{
    public class CommandesController : Controller
    {
       
        private readonly BrasilBurgerContext _context;

       
        public CommandesController(BrasilBurgerContext context)
        {
            _context = context;
        }

        [HttpPost]
        public async Task<IActionResult> PlaceOrder(string orderType, string paymentMethod)
        {
            var cartJson = HttpContext.Session.GetString("Cart");
            if (string.IsNullOrEmpty(cartJson)) 
                return RedirectToAction("Index", "Catalogue");

            var cartItems = JsonSerializer.Deserialize<List<CartItem>>(cartJson);
           var total = cartItems.Sum(i => i.Total); 

            
            var nouvelleCommande = new Commande
            {
                IdClient = 1, 
                ModeConsommation = orderType == "Sur place" ? "SUR_PLACE" : (orderType == "À emporter" ? "A_RECUPERER" : "A_LIVRER"),
                MontantTotal = total,
                Statut = "VALIDE",
                DateCommande = DateTime.Now
            };

            _context.Commandes.Add(nouvelleCommande);
            await _context.SaveChangesAsync();

            
            var nouveauPaiement = new Paiement
            {
                IdCommande = nouvelleCommande.Id, 
                Montant = total,
                ModePaiement = paymentMethod == "Orange Money" ? "OM" : "Wave",
                DatePaiement = DateTime.Now
            };

            _context.Paiements.Add(nouveauPaiement);
            await _context.SaveChangesAsync();

          
            HttpContext.Session.Remove("Cart");

           
            return View("Success", nouvelleCommande);
        }

        public IActionResult Success(Commande commande)
        {
            return View(commande);
        }
    }
}