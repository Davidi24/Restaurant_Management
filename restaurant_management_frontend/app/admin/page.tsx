import DashboardLayout from "../layouts/DashboardLayout"
import OrderCategory from "../components/order/OrderCategory"
import OrderContainer from "../components/order/OrderContainer"
import Invoices from "../components/Invoices"


function Admin() {
  return (
    <div>
      <DashboardLayout>
        <div className=" sm:ml-64  relative">
          <div className="p-4 mt-16  bg-amber-400">
            <OrderCategory />
            <OrderContainer />
            <div className="fixed right-0 top-16 overflow-y-auto">
              <Invoices />
            </div>

          </div>
        </div>
      </DashboardLayout>
    </div>
  )
}

export default Admin